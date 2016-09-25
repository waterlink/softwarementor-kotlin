package softwarementor.fixtures

import softwarementor.Context
import softwarementor.login.Login
import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.mentee.InMemoryMenteeGateway
import softwarementor.mentor.*
import softwarementor.mentorship_request.*
import softwarementor.signup.EmailConfirmationService
import softwarementor.signup.SignUp
import softwarementor.user.CurrentUserRepository
import softwarementor.signup.InMemoryUserConfirmationGateway
import softwarementor.user.InMemoryUserGateway

class SoftwareMentorFixture : MentorFixture, UserFixture, MentorshipRequestFixture, LoginFixture, SignUpFixture {
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
    override var theResponse = ""

    override val userGateway = InMemoryUserGateway()
    override val menteeGateway = InMemoryMenteeGateway()
    override val mentorGateway = InMemoryMentorGateway()
    override val mentorshipRequestGateway = InMemoryMentorshipRequestGateway()

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
}

