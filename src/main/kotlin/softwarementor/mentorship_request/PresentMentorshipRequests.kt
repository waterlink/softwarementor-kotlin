package softwarementor.mentorship_request

import softwarementor.Context

class PresentMentorshipRequests {
    fun presentMyMentorshipRequests(): List<PresentedMentorshipRequest> {
        val mentee = Context.currentMenteeRepository.currentMentee!!
        return Context.mentorshipRequestGateway.findByMenteeName(mentee.name).map {
            PresentedMentorshipRequest()
        }
    }

}