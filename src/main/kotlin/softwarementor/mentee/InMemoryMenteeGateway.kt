package softwarementor.mentee

class InMemoryMenteeGateway : MenteeGateway {
    private val entities: MutableMap<String, MenteeStoredInMemory> = mutableMapOf()

    override fun save(mentee: Mentee) {
        entities[mentee.name] = MenteeStoredInMemory(mentee)
    }

    override fun findAll() = entities.values
            .map(MenteeStoredInMemory::constructMentee)

    override fun findByName(name: String) =
            entities[name]?.constructMentee()

    class MenteeStoredInMemory(mentee: Mentee) {
        val name = mentee.name

        fun constructMentee() = Mentee(name)
    }

}
