package softwarementor.mentee

class InMemoryMenteeGateway : MenteeGateway {
    private val entities: MutableList<Mentee> = mutableListOf()

    override fun save(mentee: Mentee) {
        entities.add(mentee)
    }

    override fun findAll() = entities

    override fun findByName(name: String) =
            entities.find { it.name == name }

}