package softwarementor

import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.mentee.MenteeGateway
import softwarementor.mentor.CurrentMentorRepository
import softwarementor.mentor.MentorGateway
import softwarementor.mentorship_request.MentorshipRequestGateway
import softwarementor.user.CurrentUserRepository
import softwarementor.signup.UserConfirmationGateway
import softwarementor.user.UserGateway

class Context {
    lateinit var currentMenteeRepository: CurrentMenteeRepository
    lateinit var currentMentorRepository: CurrentMentorRepository
    lateinit var userGateway: UserGateway
    lateinit var menteeGateway: MenteeGateway
    lateinit var userConfirmationGateway: UserConfirmationGateway
    lateinit var currentUserRepository: CurrentUserRepository
    lateinit var mentorGateway: MentorGateway
    lateinit var mentorshipRequestGateway: MentorshipRequestGateway
}

