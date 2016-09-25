package softwarementor.mentee

interface MenteeGateway {
    fun save(mentee: Mentee)

    fun findByName(name: String): Mentee?

    fun findAll(): List<Mentee>

}