package softwarementor.fixtures

import softwarementor.mentee.MenteeRoleCreator
import softwarementor.mentor.MentorRoleCreator
import softwarementor.signup.EmailConfirmationService
import softwarementor.signup.InvalidConfirmationCode
import softwarementor.signup.SignUp
import softwarementor.signup.UserAlreadyRegistered

interface SignUpFixture {
    var theResponse: String

    val signUp: SignUp
    val emailConfirmationService: EmailConfirmationService

    @AcceptanceMethod
    fun whenSigningUpWithNameAndEmailAndPassword(name: String, email: String, password: String): Boolean {
        try {
            signUp.signUp(name, email, password, MenteeRoleCreator())
            theResponse = "SUCCESS"
        } catch (exception: UserAlreadyRegistered) {
            theResponse = "USER_ALREADY_REGISTERED"
        }
        return true
    }

    @AcceptanceMethod
    fun whenSigningUpAsMentorWithNameAndEmailAndPassword(name: String, email: String, password: String): Boolean {
        try {
            signUp.signUp(name, email, password, MentorRoleCreator())
            theResponse = "SUCCESS"
        } catch (exception: UserAlreadyRegistered) {
            theResponse = "USER_ALREADY_REGISTERED"
        }
        return true
    }

    @AcceptanceMethod
    fun whenConfirmingEmailWithCode(confirmationCode: String): Boolean {
        try {
            signUp.confirm(confirmationCode)
            theResponse = "SUCCESS"
        } catch (exception: InvalidConfirmationCode) {
            theResponse = "INVALID_CONFIRMATION_CODE"
        }
        return true
    }

    @AcceptanceMethod
    fun lastConfirmationEmailIsSentTo(): String {
        return emailConfirmationService.lastEmailSent.email
    }

    @AcceptanceMethod
    fun lastConfirmationEmailSCode(): String {
        return emailConfirmationService.lastEmailSent.confirmationCode
    }
}

