package softwarementor.mentorship_request

import softwarementor.Context
import softwarementor.mentor.Mentor
import softwarementor.mentee.Mentee
import softwarementor.login.NeedToSignIn

class ApplyForMentorshipWithMentor(private val context: Context) {
    fun applyForMentorshipWith(mentor: Mentor) {
        if (context.currentMenteeRepository.isGuest())
            throw NeedToSignIn()

        val mentee = context.currentMenteeRepository.currentMentee!!
        context.mentorshipRequestGateway.save(MentorshipRequest(mentee, mentor))
    }

}