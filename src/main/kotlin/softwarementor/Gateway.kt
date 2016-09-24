package softwarementor

import softwarementor.mentor.Mentor
import softwarementor.mentorship_request.MentorshipRequest
import softwarementor.mentee.Mentee

interface Gateway {
    fun save(mentor: Mentor)

    fun save(mentorshipRequest: MentorshipRequest)

    fun save(mentee: Mentee)

    fun delete(mentor: Mentor)

    fun findAllMentors(): List<Mentor>

    fun findMentorsByLanguage(language: String): List<Mentor>

    fun findMentorsByName(name: String): List<Mentor>

    fun findMentorById(mentorId: String): Mentor

    fun findAllMentorshipRequests(): List<MentorshipRequest>

    fun findMentorshipRequestsByMentee(mentee: Mentee): List<MentorshipRequest>

    fun findAllMentees(): List<Mentee>

    fun findMenteesByName(name: String): List<Mentee>
}