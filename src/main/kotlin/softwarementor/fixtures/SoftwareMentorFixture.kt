package softwarementor.fixtures

import softwarementor.Context
import softwarementor.incoming_mentorship_request.PresentIncomingMentorshipRequests
import softwarementor.incoming_mentorship_request.PresentedIncomingMentorshipRequest
import softwarementor.login.EmailNotConfirmed
import softwarementor.login.InvalidNamePassword
import softwarementor.login.Login
import softwarementor.login.NeedToSignIn
import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.mentee.InMemoryMenteeGateway
import softwarementor.mentor.*
import softwarementor.mentorship_request.*
import softwarementor.signup.EmailConfirmationService
import softwarementor.signup.SignUp
import softwarementor.user.CurrentUserRepository
import softwarementor.signup.InMemoryUserConfirmationGateway
import softwarementor.signup.UserAlreadyRegistered
import softwarementor.user.InMemoryUserGateway

class SoftwareMentorFixture : CommandExecutor.FailingCommandHandler, MentorFixture, UserFixture,
        MentorshipRequestFixture, LoginFixture, SignUpFixture,
        IncomingMentorshipRequestFixture {
    private val context = Context()

    override val presentAvailableMentorsForLanguage = PresentAvailableMentorsForLanguage(context)

    override var availableMentors: List<PresentedMentor>? = null
    override val applyForMentorshipWithMentor = ApplyForMentorshipWithMentor(context)

    override val presentMentorshipRequests = PresentMentorshipRequests(context)
    override var mentorshipRequests: List<PresentedMentorshipRequest>? = null
    override val currentMenteeRepository = CurrentMenteeRepository()

    override val currentMentorRepository = CurrentMentorRepository()

    override val emailConfirmationService = EmailConfirmationService()
    override val signUp = SignUp(context, emailConfirmationService)
    override val login = Login(context)
    override val currentUserRepository = CurrentUserRepository()

    override val userGateway = InMemoryUserGateway()
    override val menteeGateway = InMemoryMenteeGateway()
    override val mentorGateway = InMemoryMentorGateway()
    override val mentorshipRequestGateway = InMemoryMentorshipRequestGateway(context)
    override val presentIncomingMentorshipRequests = PresentIncomingMentorshipRequests(context)

    override var incomingMentorshipRequests: List<PresentedIncomingMentorshipRequest>? = null

    override val executor = CommandExecutor(this)
    var theResponse = ""

    init {
        context.userGateway = userGateway
        context.menteeGateway = menteeGateway
        context.mentorGateway = mentorGateway
        context.userConfirmationGateway = InMemoryUserConfirmationGateway()
        context.mentorshipRequestGateway = mentorshipRequestGateway

        context.currentMenteeRepository = currentMenteeRepository
        context.currentMentorRepository = currentMentorRepository
        context.currentUserRepository = currentUserRepository
    }

    @AcceptanceMethod
    fun response(): String = theResponse

    override fun handleError(exception: Exception) {
        theResponse = when (exception) {
            is NeedToSignIn -> "NEED_TO_SIGN_IN"
            is InvalidNamePassword -> "INVALID_NAME_PASSWORD"
            is EmailNotConfirmed -> "EMAIL_NOT_CONFIRMED"
            is UserAlreadyRegistered -> "USER_ALREADY_REGISTERED"
            else -> "UNEXPECTED_EXCEPTION: ${exception}"
        }
    }

    override fun handleSuccess() {
        theResponse = "SUCCESS"
    }
}

