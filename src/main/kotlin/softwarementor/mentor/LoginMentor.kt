package softwarementor.mentor

import softwarementor.Context
import softwarementor.login.EmailNotConfirmed
import softwarementor.login.InvalidNamePassword

class LoginMentor {
    fun login(name: String, password: String) {
        val mentor = Context.gateway.findMentorsByName(name).firstOrNull()

        if (mentor == null || mentor.password != password)
            throw InvalidNamePassword()

        if (!mentor.isConfirmed)
            throw EmailNotConfirmed()

        Context.currentMentorRepository.assume(mentor)
    }

    fun logout() {
        Context.currentMentorRepository.assumeGuest()
    }
}