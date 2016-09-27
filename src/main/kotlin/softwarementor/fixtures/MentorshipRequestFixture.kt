package softwarementor.fixtures

import softwarementor.mentee.MenteeGateway
import softwarementor.mentor.MentorGateway
import softwarementor.mentor.PresentedMentor
import softwarementor.mentorship_request.*

interface MentorshipRequestFixture {
    val menteeGateway: MenteeGateway
    val mentorGateway: MentorGateway
    val mentorshipRequestGateway: MentorshipRequestGateway

    val availableMentors: List<PresentedMentor>?

    val applyForMentorshipWithMentor: ApplyForMentorshipWithMentor

    val presentMentorshipRequests: PresentMentorshipRequests
    var mentorshipRequests: List<PresentedMentorshipRequest>?

    val executor: CommandExecutor

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

        executor.execute {
            applyForMentorshipWithMentor.applyForMentorshipWith(mentor)
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