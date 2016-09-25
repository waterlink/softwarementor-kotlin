package softwarementor.fixtures

import softwarementor.login.EmailNotConfirmed
import softwarementor.login.InvalidNamePassword
import softwarementor.login.Login

interface LoginFixture {
    val login: Login
    var theResponse: String

    @AcceptanceMethod
    fun whenLoggingInWithNameAndPassword(name: String, password: String): Boolean {
        try {
            login.login(name, password)
            theResponse = "SUCCESS"
        } catch (exception: InvalidNamePassword) {
            theResponse = "INVALID_NAME_PASSWORD"
        } catch (exception: EmailNotConfirmed) {
            theResponse = "EMAIL_NOT_CONFIRMED"
        }

        return true
    }

}

