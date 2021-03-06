package softwarementor.mentorship_request

interface MentorshipRequestGateway {
    fun save(mentorshipRequest: MentorshipRequest)

    fun findAll(): List<MentorshipRequest>

    fun findByMenteeName(menteeName: String): List<MentorshipRequest>

    fun findByMentorName(mentorName: String): List<MentorshipRequest>

}