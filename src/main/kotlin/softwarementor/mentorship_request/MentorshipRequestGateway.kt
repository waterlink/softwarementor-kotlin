package softwarementor.mentorship_request

interface MentorshipRequestGateway {
    fun save(mentorshipRequest: MentorshipRequest)

    fun findAll(): List<MentorshipRequest>

    fun findByMenteeName(name: String): List<MentorshipRequest>

}