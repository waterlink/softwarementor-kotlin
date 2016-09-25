package softwarementor.fixtures

import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.mentee.Mentee
import softwarementor.mentee.MenteeGateway
import softwarementor.mentor.CurrentMentorRepository
import softwarementor.mentor.Mentor
import softwarementor.mentor.MentorGateway
import softwarementor.user.CurrentUserRepository
import softwarementor.user.User
import softwarementor.user.UserGateway

interface UserFixture {
    val currentMenteeRepository: CurrentMenteeRepository
    val currentMentorRepository: CurrentMentorRepository
    val currentUserRepository: CurrentUserRepository

    val userGateway: UserGateway
    val menteeGateway: MenteeGateway
    val mentorGateway: MentorGateway

    @AcceptanceMethod
    fun givenUserIsNotAMentee(): Boolean {
        currentMenteeRepository.assumeGuest()
        return currentMenteeRepository.isGuest()
    }

    @AcceptanceMethod
    fun givenThereIsAUserWithNameAndPassword(name: String, password: String): Boolean {
        userGateway.save(User(name, "sampleEmail@example.org", password).confirmed())
        return userGateway.findByName(name)?.isConfirmed == true
    }

    @AcceptanceMethod
    fun givenThereIsAUserWithName(name: String): Boolean {
        userGateway.save(User(name, "sampleEmail@example.org", "noPassword").confirmed())
        return userGateway.findByName(name)?.isConfirmed == true
    }

    @AcceptanceMethod
    fun givenThereIsAMenteeWithName(name: String): Boolean {
        menteeGateway.save(Mentee(name))
        return menteeGateway.findAll().last().name == name
    }

    @AcceptanceMethod
    fun givenUserIsAMenteeWithName(name: String): Boolean {
        val user = menteeGateway.findByName(name)!!
        currentMenteeRepository.assume(user)
        return currentMenteeRepository.currentMentee?.name == name
    }

    @AcceptanceMethod
    fun givenUserWithNameHasRole(name: String, role: String): Boolean {
        if (role == "mentee") {
            menteeGateway.save(Mentee(name))
            return menteeGateway.findByName(name) != null
        }

        if (role == "mentor") {
            mentorGateway.save(Mentor(name, ""))
            return mentorGateway.findByName(name) != null
        }

        return false
    }

    @AcceptanceMethod
    fun currentMentee(): String? {
        return currentMenteeRepository.currentMentee?.name ?: "NOBODY"
    }

    @AcceptanceMethod
    fun currentMentor(): String? {
        return currentMentorRepository.currentMentor?.name ?: "NOBODY"
    }

    @AcceptanceMethod
    fun currentUser(): String {
        return currentUserRepository.currentUser?.name ?: "NOBODY"
    }

    fun User.confirmed(): User {
        isConfirmed = true
        userGateway.save(this)
        return this
    }

}

