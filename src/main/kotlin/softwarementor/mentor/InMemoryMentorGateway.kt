package softwarementor.mentor

class InMemoryMentorGateway : MentorGateway {
    private val entities: MutableMap<String, MentorStoredInMemory> = mutableMapOf()

    override fun save(mentor: Mentor) {
        entities[mentor.name] = MentorStoredInMemory(mentor)
    }

    override fun findByName(name: String) =
            entities[name]?.constructMentor()

    override fun findAll() = entities.values
            .map(MentorStoredInMemory::constructMentor)

    override fun findByLanguage(language: String) =
            findAll().filter { it.language == language }

    override fun delete(mentor: Mentor) {
        entities.remove(mentor.name)
    }

    class MentorStoredInMemory(mentor: Mentor) {
        val name = mentor.name
        val language = mentor.language

        fun constructMentor() = Mentor(name, language)

    }

}
