package softwarementor.signup_mentee

import softwarementor.Context
import softwarementor.signup_mentee.MenteeConfirmationEmailService
import softwarementor.signup.InvalidConfirmationCode
import softwarementor.signup_mentee.MenteeConfirmation
import softwarementor.signup.UserAlreadyRegistered
import softwarementor.mentee.Mentee
import java.util.*

class SignUpMentee(private val menteeConfirmationEmailService: MenteeConfirmationEmailService) {
    fun signUp(name: String, email: String, password: String) {
        val mentee = Context.gateway.findMenteeByName(name)

        if (mentee != null)
            throw UserAlreadyRegistered()

        val newMentee = Mentee(name, email, password)
        Context.gateway.save(newMentee)

        val confirmationCode = UUID.randomUUID().toString()
        val menteeConfirmation = MenteeConfirmation(name, confirmationCode)
        Context.gateway.save(menteeConfirmation)
        menteeConfirmationEmailService.sendConfirmationEmail(email, name, confirmationCode)
    }

    fun confirm(confirmationCode: String) {
        val menteeConfirmation = Context.gateway.findMenteeConfirmationByCode(confirmationCode)
                ?: throw InvalidConfirmationCode()

        val mentee = Context.gateway.findMenteeByName(menteeConfirmation.menteeName)
        mentee!!.isConfirmed = true
    }
}