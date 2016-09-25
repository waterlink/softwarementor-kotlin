package softwarementor.mentorship_request

import softwarementor.Context

class PresentMentorshipRequests(private val context: Context) {
    fun presentMyMentorshipRequests(): List<PresentedMentorshipRequest> {
        val mentee = context.currentMenteeRepository.currentMentee!!
        return context.mentorshipRequestGateway.findByMenteeName(mentee.name).map {
            PresentedMentorshipRequest()
        }
    }

}