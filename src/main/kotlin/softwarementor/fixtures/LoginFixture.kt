package softwarementor.fixtures

import softwarementor.login.EmailNotConfirmed
import softwarementor.login.InvalidNamePassword
import softwarementor.mentee.LoginMentee
import softwarementor.mentor.LoginMentor

interface LoginFixture {
    val loginMentee: LoginMentee
    val loginMentor: LoginMentor

    var theResponse: String

    @AcceptanceMethod
    fun whenLoggingInWithNameAndPassword(name: String, password: String): Boolean {
        logout()

        try {
            loginMentee.login(name, password)
            theResponse = "SUCCESS"
        } catch (exception: InvalidNamePassword) {
            theResponse = "INVALID_NAME_PASSWORD"
        } catch (exception: EmailNotConfirmed) {
            theResponse = "EMAIL_NOT_CONFIRMED"
        }

        return true
    }

    @AcceptanceMethod
    fun whenLoggingInAsAMentorWithNameAndPassword(name: String, password: String): Boolean {
        logoutMentor()

        try {
            loginMentor.login(name, password)
            theResponse = "SUCCESS"
        } catch (exception: InvalidNamePassword) {
            theResponse = "INVALID_NAME_PASSWORD"
        } catch (exception: EmailNotConfirmed) {
            theResponse = "EMAIL_NOT_CONFIRMED"
        }

        return true
    }

    @AcceptanceMethod
    fun logout(): Boolean {
        loginMentee.logout()
        return true
    }

    @AcceptanceMethod
    fun logoutMentor(): Boolean {
        loginMentor.logout()
        return true
    }
}

