package softwarementor

import softwarementor.mentor.Mentor
import softwarementor.mentorship_request.MentorshipRequest
import java.util.*

class InMemoryGateway : Gateway {
    private val mentors: MutableList<Mentor> = mutableListOf()
    private val mentorshipRequests: MutableList<MentorshipRequest> = mutableListOf()

    override fun save(mentor: Mentor) {
        mentor.id = UUID.randomUUID().toString()
        mentors.add(mentor)
    }

    override fun save(mentorshipRequest: MentorshipRequest) {
        mentorshipRequests.add(mentorshipRequest)
    }

    override fun delete(mentor: Mentor) {
        mentors.remove(mentor)
    }

    override fun findAllMentors() = mentors

    override fun findAllMentorshipRequests() = mentorshipRequests

    override fun findMentorById(mentorId: String) =
            mentors.filter { it.id == mentorId }.first()

    override fun findMentorsByLanguage(language: String) =
            mentors.filter { it.language == language }
}