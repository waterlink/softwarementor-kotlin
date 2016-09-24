package softwarementor.mentor

import softwarementor.Context
import softwarementor.login.InvalidNamePassword

class LoginMentor {
    fun login(name: String, password: String) {
        val mentor = Context.gateway.findMentorsByName(name).firstOrNull()

        if (mentor?.password != password)
            throw InvalidNamePassword()

        Context.currentMentorRepository.assume(mentor!!)
    }
}