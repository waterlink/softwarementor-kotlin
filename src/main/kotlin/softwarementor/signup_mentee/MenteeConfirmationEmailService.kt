package softwarementor.signup_mentee

import softwarementor.signup_mentee.MenteeConfirmationEmail

class MenteeConfirmationEmailService {
    var lastEmailSent: MenteeConfirmationEmail = MenteeConfirmationEmail("NOBODY", "CONFIRMATION_CODE")

    fun sendConfirmationEmail(email: String, name: String, confirmationCode: String) {
        lastEmailSent = MenteeConfirmationEmail(email, confirmationCode)
    }
}