package softwarementor

import softwarementor.fixtures.MentorConfirmation
import softwarementor.signup_mentee.MenteeConfirmation
import softwarementor.mentor.Mentor
import softwarementor.mentorship_request.MentorshipRequest
import softwarementor.mentee.Mentee

interface Gateway {
    fun save(mentor: Mentor)

    fun save(mentorshipRequest: MentorshipRequest)

    fun save(mentee: Mentee)

    fun save(menteeConfirmation: MenteeConfirmation)

    fun save(mentorConfirmation: MentorConfirmation)

    fun delete(mentor: Mentor)

    fun findAllMentors(): List<Mentor>

    fun findMentorsByLanguage(language: String): List<Mentor>

    fun findMentorsByName(name: String): List<Mentor>

    fun findMentorById(mentorId: String): Mentor

    fun findAllMentorshipRequests(): List<MentorshipRequest>

    fun findMentorshipRequestsByMentee(mentee: Mentee): List<MentorshipRequest>

    fun findAllMentees(): List<Mentee>

    fun findMenteesByName(name: String): List<Mentee>

    fun findMenteeByName(name: String): Mentee?

    fun findMenteeByNameAndEmail(name: String, email: String): Mentee?

    fun findMenteeConfirmationByCode(confirmationCode: String): MenteeConfirmation?

    fun findMentorConfirmationByCode(confirmationCode: String): MentorConfirmation?

    fun findMentorByName(name: String): Mentor?
}