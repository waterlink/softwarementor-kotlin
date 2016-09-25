package softwarementor.mentorship_request

class InMemoryMentorshipRequestGateway: MentorshipRequestGateway {
    private val entities: MutableList<MentorshipRequest> = mutableListOf()

    override fun save(mentorshipRequest: MentorshipRequest) {
        entities.add(mentorshipRequest)
    }

    override fun findAll() = entities

    override fun findByMenteeName(name: String) =
            entities.filter { it.mentee.name == name }
}