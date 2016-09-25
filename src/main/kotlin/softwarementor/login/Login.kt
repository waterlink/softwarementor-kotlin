package softwarementor.login

import softwarementor.Context

class Login(private val context: Context) {
    fun login(name: String, password: String) {
        logout()

        val user = context.userGateway.findByName(name)

        if (user == null || user.password != password)
            throw InvalidNamePassword()

        if (!user.isConfirmed)
            throw EmailNotConfirmed()

        context.currentUserRepository.assume(user)

        val mentee = context.menteeGateway.findByName(name)
        context.currentMenteeRepository.assume(mentee)

        val mentor = context.mentorGateway.findByName(name)
        context.currentMentorRepository.assume(mentor)
    }

    fun logout() {
        context.currentUserRepository.assumeGuest()
        context.currentMenteeRepository.assumeGuest()
        context.currentMentorRepository.assumeGuest()
    }

}