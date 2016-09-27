package softwarementor.fixtures

import softwarementor.signup.EmailConfirmationService
import softwarementor.signup.InvalidConfirmationCode
import softwarementor.signup.SignUp
import softwarementor.signup.UserAlreadyRegistered

interface SignUpFixture {
    val signUp: SignUp
    val emailConfirmationService: EmailConfirmationService
    val executor: CommandExecutor

    @AcceptanceMethod
    fun whenSigningUpWithNameAndEmailAndPassword(name: String, email: String, password: String): Boolean {
        executor.execute {
            signUp.signUpAsMentee(name, email, password)
        }
        return true
    }

    @AcceptanceMethod
    fun whenSigningUpAsMentorWithNameAndEmailAndPassword(name: String, email: String, password: String): Boolean {
        executor.execute {
            signUp.signUpAsMentor(name, email, password)
        }
        return true
    }

    @AcceptanceMethod
    fun whenConfirmingEmailWithCode(confirmationCode: String): Boolean {
        executor.execute {
            signUp.confirm(confirmationCode)
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

