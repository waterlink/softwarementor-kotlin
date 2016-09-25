package softwarementor.login

import softwarementor.Context

class Login {
    fun login(name: String, password: String) {
        logout()

        val user = Context.userGateway.findByName(name)

        if (user == null || user.password != password)
            throw InvalidNamePassword()

        if (!user.isConfirmed)
            throw EmailNotConfirmed()

        Context.currentUserRepository.assume(user)

        val mentee = Context.menteeGateway.findByName(name)
        Context.currentMenteeRepository.assume(mentee)

        val mentor = Context.mentorGateway.findByName(name)
        Context.currentMentorRepository.assume(mentor)
    }

    fun logout() {
        Context.currentUserRepository.assumeGuest()
        Context.currentMenteeRepository.assumeGuest()
        Context.currentMentorRepository.assumeGuest()
    }

}