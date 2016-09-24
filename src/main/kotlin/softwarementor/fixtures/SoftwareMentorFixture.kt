package softwarementor.fixtures

import softwarementor.Context
import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.Gateway
import softwarementor.InMemoryGateway
import softwarementor.mentee.LoginMentee
import softwarementor.mentor.CurrentMentorRepository
import softwarementor.mentor.LoginMentor
import softwarementor.mentorship_request.ApplyForMentorshipWithMentor
import softwarementor.mentor.PresentAvailableMentorsForLanguage
import softwarementor.mentor.PresentedMentor
import softwarementor.mentorship_request.PresentMentorshipRequests
import softwarementor.mentorship_request.PresentedMentorshipRequest

class SoftwareMentorFixture : MentorFixture, UserFixture, MentorshipRequestFixture, LoginFixture {
    override val gateway: Gateway = InMemoryGateway()

    override val presentAvailableMentorsForLanguage = PresentAvailableMentorsForLanguage()
    override var availableMentors: List<PresentedMentor>? = null

    override val applyForMentorshipWithMentor = ApplyForMentorshipWithMentor()
    override val presentMentorshipRequests = PresentMentorshipRequests()
    override var mentorshipRequests: List<PresentedMentorshipRequest>? = null

    override val loginMentee = LoginMentee()
    override val loginMentor = LoginMentor()

    override val currentMenteeRepository = CurrentMenteeRepository()
    override val currentMentorRepository = CurrentMentorRepository()

    override var theResponse = ""

    init {
        Context.gateway = gateway
        Context.currentMenteeRepository = currentMenteeRepository
        Context.currentMentorRepository = currentMentorRepository
    }

    @AcceptanceMethod
    fun response(): String = theResponse
}