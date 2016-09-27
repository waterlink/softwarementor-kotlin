package softwarementor.mentorship_request

import softwarementor.Context

class InMemoryMentorshipRequestGateway(private val context: Context) : MentorshipRequestGateway {
    private val entities: MutableList<MentorshipRequestStoredInMemory> = mutableListOf()

    override fun save(mentorshipRequest: MentorshipRequest) {
        entities.add(MentorshipRequestStoredInMemory(mentorshipRequest))
    }

    override fun findAll() = entities
            .map { it.constructMentorshipRequest(context) }

    override fun findByMenteeName(menteeName: String) =
            findAll().filter { it.mentee.name == menteeName }

    override fun findByMentorName(mentorName: String) =
            findAll().filter { it.mentor.name == mentorName }

    class MentorshipRequestStoredInMemory(mentorshipRequest: MentorshipRequest) {
        val menteeName = mentorshipRequest.mentee.name
        val mentorName = mentorshipRequest.mentor.name

        fun constructMentorshipRequest(context: Context): MentorshipRequest {
            val mentee = context.menteeGateway.findByName(menteeName)!!
            val mentor = context.mentorGateway.findByName(mentorName)!!
            return MentorshipRequest(mentee, mentor)
        }

    }

}
