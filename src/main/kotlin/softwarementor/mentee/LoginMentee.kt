package softwarementor.mentee

import softwarementor.Context
import softwarementor.login.EmailNotConfirmed
import softwarementor.login.InvalidNamePassword

class LoginMentee {
    fun login(name: String, password: String) {
        val mentee = Context.gateway.findMenteesByName(name).firstOrNull()

        if (mentee == null || mentee.password != password)
            throw InvalidNamePassword()

        if (!mentee.isConfirmed)
            throw EmailNotConfirmed()

        Context.currentMenteeRepository.assume(mentee)
    }

    fun logout() {
        Context.currentMenteeRepository.assumeGuest()
    }
}