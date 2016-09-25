package softwarementor.fixtures

import softwarementor.mentor.PresentedMentor
import softwarementor.login.NeedToSignIn
import softwarementor.mentee.MenteeGateway
import softwarementor.mentor.MentorGateway
import softwarementor.mentorship_request.*

interface MentorshipRequestFixture {
    val menteeGateway: MenteeGateway
    val mentorGateway: MentorGateway
    val mentorshipRequestGateway: MentorshipRequestGateway

    val availableMentors: List<PresentedMentor>?

    val applyForMentorshipWithMentor: ApplyForMentorshipWithMentor
    var theResponse: String

    val presentMentorshipRequests: PresentMentorshipRequests
    var mentorshipRequests: List<PresentedMentorshipRequest>?

    @AcceptanceMethod
    fun givenThereIsAMentorshipRequestFromTo(fromMentee: String, toMentor: String): Boolean {
        val mentee = menteeGateway.findByName(fromMentee)!!
        val mentor = mentorGateway.findByName(toMentor)!!

        mentorshipRequestGateway.save(MentorshipRequest(mentee, mentor))

        val mentorshipRequest = mentorshipRequestGateway.findAll().last()
        return mentorshipRequest.mentee.name == fromMentee &&
                mentorshipRequest.mentor.name == toMentor
    }

    @AcceptanceMethod
    fun whenApplyingForMentorshipWithMentor(mentorName: String): Boolean {
        val mentor = mentorGateway.findByName(mentorName)!!

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
    fun countOfMentorshipRequests() =
            mentorshipRequests?.count()
}

