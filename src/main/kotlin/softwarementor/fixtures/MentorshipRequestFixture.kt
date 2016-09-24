package softwarementor.fixtures

import softwarementor.Gateway
import softwarementor.mentorship_request.ApplyForMentorshipWithMentor
import softwarementor.user.NeedToSignIn
import softwarementor.mentor.PresentedMentor
import softwarementor.mentorship_request.PresentMentorshipRequests
import softwarementor.mentorship_request.PresentedMentorshipRequest

interface MentorshipRequestFixture {
    val gateway: Gateway
    val availableMentors: List<PresentedMentor>?

    val applyForMentorshipWithMentor: ApplyForMentorshipWithMentor
    var response: String

    val presentMentorshipRequests: PresentMentorshipRequests
    var mentorshipRequests: List<PresentedMentorshipRequest>?

    @AcceptanceMethod
    fun whenApplyingForMentorshipWithMentor(userFriendlyIndex: Int): Boolean {
        val mentorId = availableMentors!![userFriendlyIndex - 1].id
        val mentor = gateway.findMentorById(mentorId!!)

        try {
            applyForMentorshipWithMentor.applyForMentorshipWith(mentor)
            response = "SUCCESS"
        } catch (exception: NeedToSignIn) {
            response = "NEED_TO_SIGN_IN"
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

