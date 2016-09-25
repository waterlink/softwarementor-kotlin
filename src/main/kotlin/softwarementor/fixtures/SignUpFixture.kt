package softwarementor.fixtures

import softwarementor.signup.InvalidConfirmationCode
import softwarementor.signup.UserAlreadyRegistered
import softwarementor.signup_mentee.MenteeConfirmationEmailService
import softwarementor.signup_mentee.SignUpMentee
import softwarementor.signup_mentor.MentorConfirmationEmailService
import softwarementor.signup_mentor.SignUpMentor

interface SignUpFixture {
    val menteeConfirmationEmailService: MenteeConfirmationEmailService
    val mentorConfirmationEmailService: MentorConfirmationEmailService
    val signUpMentee: SignUpMentee
    val signUpMentor: SignUpMentor
    var theResponse: String

    @AcceptanceMethod
    fun whenSigningUpWithNameAndEmailAndPassword(name: String, email: String, password: String): Boolean {
        try {
            signUpMentee.signUp(name, email, password)
            theResponse = "SUCCESS"
        } catch (exception: UserAlreadyRegistered) {
            theResponse = "USER_ALREADY_REGISTERED"
        }
        return true
    }

    @AcceptanceMethod
    fun whenSigningUpAsMentorWithNameAndEmailAndPassword(name: String, email: String, password: String): Boolean {
        try {
            signUpMentor.signUp(name, email, password)
            theResponse = "SUCCESS"
        } catch (exception: UserAlreadyRegistered) {
            theResponse = "USER_ALREADY_REGISTERED"
        }
        return true
    }

    @AcceptanceMethod
    fun whenConfirmingEmailWithCode(confirmationCode: String): Boolean {
        try {
            signUpMentee.confirm(confirmationCode)
            theResponse = "SUCCESS"
        } catch (exception: InvalidConfirmationCode) {
            theResponse = "INVALID_CONFIRMATION_CODE"
        }
        return true
    }

    @AcceptanceMethod
    fun whenConfirmingMentorEmailWithCode(confirmationCode: String): Boolean {
        try {
            signUpMentor.confirm(confirmationCode)
            theResponse = "SUCCESS"
        } catch (exception: InvalidConfirmationCode) {
            theResponse = "INVALID_CONFIRMATION_CODE"
        }
        return true
    }

    @AcceptanceMethod
    fun lastConfirmationEmailIsSentTo(): String {
        return menteeConfirmationEmailService.lastEmailSent.email
    }

    @AcceptanceMethod
    fun lastMentorConfirmationEmailIsSentTo(): String {
        return mentorConfirmationEmailService.lastEmailSent.email
    }

    @AcceptanceMethod
    fun lastConfirmationEmailSCode(): String {
        return menteeConfirmationEmailService.lastEmailSent.confirmationCode
    }

    @AcceptanceMethod
    fun lastMentorConfirmationEmailSCode(): String {
        return mentorConfirmationEmailService.lastEmailSent.confirmationCode
    }
}