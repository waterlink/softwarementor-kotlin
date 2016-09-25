package softwarementor.signup_mentor

import softwarementor.Context
import softwarementor.signup_mentor.MentorConfirmation
import softwarementor.mentor.Mentor
import java.util.*

class SignUpMentor(private val mentorConfirmationEmailService: MentorConfirmationEmailService) {

    fun signUp(name: String, email: String, password: String) {
        val mentor = Mentor(name, email, password, "");
        Context.gateway.save(mentor)

        val code = UUID.randomUUID().toString()
        Context.gateway.save(MentorConfirmation(name, code))
        mentorConfirmationEmailService.sendConfirmationEmail(email, name, code)
    }

    fun confirm(confirmationCode: String) {
        val mentorConfirmation = Context.gateway.findMentorConfirmationByCode(confirmationCode)
        val mentor = Context.gateway.findMentorByName(mentorConfirmation!!.mentorName)
        mentor?.isConfirmed = true
    }
}