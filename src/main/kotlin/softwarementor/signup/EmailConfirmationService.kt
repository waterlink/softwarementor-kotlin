package softwarementor.signup

import softwarementor.signup.ConfirmationEmail

class EmailConfirmationService {
    var lastEmailSent = ConfirmationEmail("NOBODY", "CONFIRMATION_CODE")

    fun sendConfirmationEmail(email: String, name: String, confirmationCode: String) {
        lastEmailSent = ConfirmationEmail(email, confirmationCode)
    }
}