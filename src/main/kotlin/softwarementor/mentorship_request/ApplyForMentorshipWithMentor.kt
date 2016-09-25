package softwarementor.mentorship_request

import softwarementor.Context
import softwarementor.mentor.Mentor
import softwarementor.mentee.Mentee
import softwarementor.login.NeedToSignIn

class ApplyForMentorshipWithMentor {
    fun applyForMentorshipWith(mentor: Mentor) {
        if (Context.currentMenteeRepository.isGuest())
            throw NeedToSignIn()

        val mentee = Context.currentMenteeRepository.currentMentee!!
        Context.mentorshipRequestGateway.save(MentorshipRequest(mentee, mentor))
    }

}