package softwarementor.signup_mentor

import softwarementor.signup_mentor.MentorConfirmationEmail

class MentorConfirmationEmailService {
    var lastEmailSent = MentorConfirmationEmail("NOBODY", "CONFIRMATION_CODE")

    fun sendConfirmationEmail(email: String, name: String, confirmationCode: String) {
        lastEmailSent = MentorConfirmationEmail(email, confirmationCode)
    }
}