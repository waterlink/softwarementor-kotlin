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
    override val presentAvailableMentorsForLanguage = PresentAvailableMentorsForLanguage()

    override var availableMentors: List<PresentedMentor>? = null
    override val applyForMentorshipWithMentor = ApplyForMentorshipWithMentor()

    override val presentMentorshipRequests = PresentMentorshipRequests()
    override var mentorshipRequests: List<PresentedMentorshipRequest>? = null
    override val currentMenteeRepository = CurrentMenteeRepository()

    override val currentMentorRepository = CurrentMentorRepository()

    override val emailConfirmationService = EmailConfirmationService()
    override val signUp = SignUp(emailConfirmationService)
    override val login = Login()
    override val currentUserRepository = CurrentUserRepository()
    override var theResponse = ""

    override val userGateway = InMemoryUserGateway()
    override val menteeGateway = InMemoryMenteeGateway()
    override val mentorGateway = InMemoryMentorGateway()
    override val mentorshipRequestGateway = InMemoryMentorshipRequestGateway()

    init {
        Context.userGateway = userGateway
        Context.menteeGateway = menteeGateway
        Context.mentorGateway = mentorGateway
        Context.userConfirmationGateway = InMemoryUserConfirmationGateway()
        Context.mentorshipRequestGateway = mentorshipRequestGateway

        Context.currentMenteeRepository = currentMenteeRepository
        Context.currentMentorRepository = currentMentorRepository
        Context.currentUserRepository = currentUserRepository
    }

    @AcceptanceMethod
    fun response(): String = theResponse
}

