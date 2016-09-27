package softwarementor.incoming_mentorship_request

import softwarementor.Context

class PresentIncomingMentorshipRequests(private val context: Context) {
    fun presentIncomingMentorshipRequests(): List<PresentedIncomingMentorshipRequest> {
        val mentor = context.currentMentorRepository.currentMentor!!
        return context.mentorshipRequestGateway.findByMentorName(mentor.name).map {
            PresentedIncomingMentorshipRequest()
        }
    }

}