package softwarementor.fixtures

import softwarementor.Gateway
import softwarementor.mentee.CurrentMenteeRepository
import softwarementor.mentee.Mentee
import softwarementor.mentor.CurrentMentorRepository
import softwarementor.mentor.Mentor

interface UserFixture {
    val currentMenteeRepository: CurrentMenteeRepository
    val currentMentorRepository: CurrentMentorRepository

    val gateway: Gateway

    @AcceptanceMethod
    fun givenUserIsAGuest(): Boolean {
        currentMenteeRepository.assumeGuest()
        return currentMenteeRepository.isGuest()
    }

    @AcceptanceMethod
    fun givenUserIsAMentee(): Boolean {
        currentMenteeRepository.assumeMentee()
        return currentMenteeRepository.isLoggedIn()
    }

    @AcceptanceMethod
    fun givenThereIsAMenteeWithName(name: String): Boolean {
        gateway.save(Mentee(name, "NO_PASSWORD"))
        return gateway.findAllMentees().last().name == name
    }

    @AcceptanceMethod
    fun givenThereIsAMenteeWithNameAndPassword(name: String, password: String): Boolean {
        gateway.save(Mentee(name, password))
        return gateway.findAllMentees().last().name == name
    }

    @AcceptanceMethod
    fun givenUserIs(name: String): Boolean {
        val user = gateway.findMenteesByName(name).first()
        currentMenteeRepository.assume(user)
        return currentMenteeRepository.currentMentee?.name == name
    }

    @AcceptanceMethod
    fun givenThereIsAMentorWithNameAndPassword(name: String, password: String): Boolean {
        gateway.save(Mentor("SOME_LANGUAGE", name, password))
        return gateway.findAllMentors().last().name == name
    }

    @AcceptanceMethod
    fun currentMentee(): String? {
        return currentMenteeRepository.currentMentee?.name
    }

    @AcceptanceMethod
    fun currentMentor(): String? {
        return currentMentorRepository.currentMentor?.name
    }
}

