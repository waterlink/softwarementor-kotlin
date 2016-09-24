package softwarementor.fixtures

import softwarementor.login.InvalidNamePassword
import softwarementor.mentee.LoginMentee
import softwarementor.mentor.LoginMentor

interface LoginFixture {
    val loginMentee: LoginMentee
    val loginMentor: LoginMentor

    var theResponse: String

    @AcceptanceMethod
    fun whenLoggingInWithNameAndPassword(name: String, password: String): Boolean {
        try {
            loginMentee.login(name, password)
            theResponse = "SUCCESS"
        } catch (exception: InvalidNamePassword) {
            theResponse = "INVALID_NAME_PASSWORD"
        }
        return true
    }

    @AcceptanceMethod
    fun whenLoggingInAsAMentorWithNameAndPassword(name: String, password: String): Boolean {
        try {
            loginMentor.login(name, password)
            theResponse = "SUCCESS"
        } catch (exception: InvalidNamePassword) {
            theResponse = "INVALID_NAME_PASSWORD"
        }

        return true
    }
}

