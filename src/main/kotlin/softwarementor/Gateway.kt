package softwarementor

import softwarementor.mentor.Mentor
import softwarementor.mentorship_request.MentorshipRequest

interface Gateway {
    fun findAllMentors(): List<Mentor>

    fun delete(mentor: Mentor)

    fun save(mentor: Mentor)

    fun findMentorsByLanguage(language: String): List<Mentor>

    fun findMentorById(mentorId: String): Mentor

    fun findAllMentorshipRequests(): List<MentorshipRequest>

    fun save(mentorshipRequest: MentorshipRequest)
}