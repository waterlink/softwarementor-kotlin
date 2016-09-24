package softwarementor.mentorship_request

import softwarementor.Context

class PresentMentorshipRequests {
    fun presentMyMentorshipRequests(): List<PresentedMentorshipRequest> {
        return Context.gateway.findAllMentorshipRequests().map {
            PresentedMentorshipRequest()
        }
    }

}