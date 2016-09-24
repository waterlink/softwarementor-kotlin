package softwarementor.mentee

import softwarementor.Context
import softwarementor.login.InvalidNamePassword

class LoginMentee {
    fun login(name: String, password: String) {
        val mentee = Context.gateway.findMenteesByName(name).firstOrNull()

        if (mentee?.password != password)
            throw InvalidNamePassword()

        Context.currentMenteeRepository.assume(mentee!!)
    }
}