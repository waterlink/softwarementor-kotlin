package softwarementor.fixtures

import softwarementor.Gateway
import softwarementor.mentor.PresentedMentor
import softwarementor.mentorship_request.ApplyForMentorshipWithMentor
import softwarementor.mentorship_request.MentorshipRequest
import softwarementor.mentorship_request.PresentMentorshipRequests
import softwarementor.mentorship_request.PresentedMentorshipRequest
import softwarementor.login.NeedToSignIn

interface MentorshipRequestFixture {
    val gateway: Gateway
    val availableMentors: List<PresentedMentor>?

    val applyForMentorshipWithMentor: ApplyForMentorshipWithMentor
    var theResponse: String

    val presentMentorshipRequests: PresentMentorshipRequests
    var mentorshipRequests: List<PresentedMentorshipRequest>?

    @AcceptanceMethod
    fun givenThereIsAMentorshipRequestFromTo(fromMentee: String, toMentor: String): Boolean {
        val mentee = gateway.findMenteesByName(fromMentee).first()
        val mentor = gateway.findMentorsByName(toMentor).first()

        gateway.save(MentorshipRequest(mentee, mentor))

        val mentorshipRequest = gateway.findAllMentorshipRequests().last()
        return mentorshipRequest.mentee.name == fromMentee &&
                mentorshipRequest.mentor.name == toMentor
    }

    @AcceptanceMethod
    fun whenApplyingForMentorshipWithMentor(mentorName: String): Boolean {
        val mentor = gateway.findMentorsByName(mentorName).first()

        try {
            applyForMentorshipWithMentor.applyForMentorshipWith(mentor)
            theResponse = "SUCCESS"
        } catch (exception: NeedToSignIn) {
            theResponse = "NEED_TO_SIGN_IN"
        }

        return true
    }

    @AcceptanceMethod
    fun whenPresentingMentorshipRequests(): Boolean {
        mentorshipRequests = presentMentorshipRequests.presentMyMentorshipRequests()
        return true
    }

    @AcceptanceMethod
    fun thenCountOfMentorshipRequestsIs(expectedCount: Int) =
            mentorshipRequests?.count() == expectedCount
}
