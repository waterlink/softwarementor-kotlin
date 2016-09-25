package softwarementor.mentor

interface MentorGateway {
    fun save(mentor: Mentor)

    fun findByName(name: String): Mentor?

    fun findAll(): List<Mentor>

    fun delete(mentor: Mentor)

    fun findByLanguage(language: String): List<Mentor>
}