package softwarementor.mentorship_request

import softwarementor.Context

class InMemoryMentorshipRequestGateway(private val context: Context) : MentorshipRequestGateway {
    private val entities: MutableList<MentorshipRequestStoredInMemory> = mutableListOf()

    override fun save(mentorshipRequest: MentorshipRequest) {
        entities.add(MentorshipRequestStoredInMemory(mentorshipRequest))
    }

    override fun findAll() = entities
            .map { it.constructMentorshipRequest(context) }

    override fun findByMenteeName(menteeName: String) = entities
            .filter { it.menteeName == menteeName }
            .map { it.constructMentorshipRequest(context) }

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
