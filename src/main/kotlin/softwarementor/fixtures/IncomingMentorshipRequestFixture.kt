package softwarementor.fixtures

import softwarementor.incoming_mentorship_request.PresentIncomingMentorshipRequests
import softwarementor.incoming_mentorship_request.PresentedIncomingMentorshipRequest

interface IncomingMentorshipRequestFixture {
    val presentIncomingMentorshipRequests: PresentIncomingMentorshipRequests

    var incomingMentorshipRequests: List<PresentedIncomingMentorshipRequest>?

    @AcceptanceMethod
    fun whenPresentingIncomingMentorshipRequests(): Boolean {
        incomingMentorshipRequests = presentIncomingMentorshipRequests.presentIncomingMentorshipRequests()
        return true
    }

    @AcceptanceMethod
    fun incomingMentorshipRequestsCount(): Int? {
        return incomingMentorshipRequests?.count()
    }
}