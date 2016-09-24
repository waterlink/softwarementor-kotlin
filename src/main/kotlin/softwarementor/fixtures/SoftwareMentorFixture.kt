package softwarementor.fixtures

import softwarementor.Context
import softwarementor.user.CurrentUserRepository
import softwarementor.Gateway
import softwarementor.InMemoryGateway
import softwarementor.mentorship_request.ApplyForMentorshipWithMentor
import softwarementor.mentor.PresentAvailableMentorsForLanguage
import softwarementor.mentor.PresentedMentor
import softwarementor.mentorship_request.PresentMentorshipRequests
import softwarementor.mentorship_request.PresentedMentorshipRequest

class SoftwareMentorFixture : MentorFixture, UserFixture, MentorshipRequestFixture {
    override val gateway: Gateway = InMemoryGateway()
    override val currentUserRepository = CurrentUserRepository()
    override var response = ""

    override val presentAvailableMentorsForLanguage = PresentAvailableMentorsForLanguage()
    override var availableMentors: List<PresentedMentor>? = null

    override val applyForMentorshipWithMentor = ApplyForMentorshipWithMentor()

    override val presentMentorshipRequests = PresentMentorshipRequests()
    override var mentorshipRequests: List<PresentedMentorshipRequest>? = null

    init {
        Context.gateway = gateway
        Context.currentUserRepository = currentUserRepository
    }

    @AcceptanceMethod
    fun thenTheResponseIs(expectedResponse: String) =
            response == expectedResponse
}