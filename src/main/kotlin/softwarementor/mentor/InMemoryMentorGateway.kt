package softwarementor.mentor

class InMemoryMentorGateway : MentorGateway {
    private val entities: MutableList<Mentor> = mutableListOf()

    override fun save(mentor: Mentor) {
        entities.add(mentor)
    }

    override fun findByName(name: String) =
            entities.find { it.name == name }

    override fun findAll() = entities

    override fun findByLanguage(language: String) =
            entities.filter { it.language == language }

    override fun delete(mentor: Mentor) {
        entities.remove(mentor)
    }
}