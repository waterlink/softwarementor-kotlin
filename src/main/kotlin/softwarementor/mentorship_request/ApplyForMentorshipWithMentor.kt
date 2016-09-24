package softwarementor.mentorship_request

import softwarementor.Context
import softwarementor.mentor.Mentor
import softwarementor.user.NeedToSignIn
import softwarementor.mentorship_request.MentorshipRequest

class ApplyForMentorshipWithMentor {
    fun applyForMentorshipWith(mentor: Mentor) {
        if (Context.currentUserRepository.isGuest())
            throw NeedToSignIn()

        Context.gateway.save(MentorshipRequest(mentor))
    }

}