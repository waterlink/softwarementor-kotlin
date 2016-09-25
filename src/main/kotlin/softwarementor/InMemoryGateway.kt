package softwarementor

import softwarementor.signup_mentor.MentorConfirmation
import softwarementor.signup_mentee.MenteeConfirmation
import softwarementor.mentor.Mentor
import softwarementor.mentorship_request.MentorshipRequest
import softwarementor.mentee.Mentee
import java.util.*

class InMemoryGateway : Gateway {
    private val mentors: MutableList<Mentor> = mutableListOf()
    private val mentorshipRequests: MutableList<MentorshipRequest> = mutableListOf()
    private val mentees: MutableList<Mentee> = mutableListOf()
    private val menteeConfirmations: MutableList<MenteeConfirmation> = mutableListOf()
    private val mentorConfirmations: MutableList<MentorConfirmation> = mutableListOf()

    override fun save(mentor: Mentor) {
        mentor.id = UUID.randomUUID().toString()
        mentors.add(mentor)
    }

    override fun save(mentorshipRequest: MentorshipRequest) {
        mentorshipRequests.add(mentorshipRequest)
    }

    override fun save(mentee: Mentee) {
        mentees.add(mentee)
    }

    override fun save(menteeConfirmation: MenteeConfirmation) {
        menteeConfirmations.add(menteeConfirmation)
    }

    override fun save(mentorConfirmation: MentorConfirmation) {
        mentorConfirmations.add(mentorConfirmation)
    }

    override fun delete(mentor: Mentor) {
        mentors.remove(mentor)
    }

    override fun findAllMentors() = mentors

    override fun findAllMentorshipRequests() = mentorshipRequests
    override fun findAllMentees() = mentees

    override fun findMentorById(mentorId: String) =
            mentors.filter { it.id == mentorId }.first()

    override fun findMentorsByLanguage(language: String) =
            mentors.filter { it.language == language }

    override fun findMentorsByName(name: String) =
            mentors.filter { it.name == name }

    override fun findMentorshipRequestsByMentee(mentee: Mentee) =
            mentorshipRequests.filter { it.mentee == mentee }

    override fun findMenteeByNameAndEmail(name: String, email: String) =
            mentees.find { it.name == name && it.email == email }

    override fun findMenteeByName(name: String) =
            findMenteesByName(name).firstOrNull()

    override fun findMenteesByName(name: String) =
            mentees.filter { it.name == name }

    override fun findMenteeConfirmationByCode(confirmationCode: String) =
            menteeConfirmations.find { it.code == confirmationCode }

    override fun findMentorByName(name: String) =
            findMentorsByName(name).firstOrNull()

    override fun findMentorConfirmationByCode(confirmationCode: String) =
            mentorConfirmations.find { it.code == confirmationCode }
}
