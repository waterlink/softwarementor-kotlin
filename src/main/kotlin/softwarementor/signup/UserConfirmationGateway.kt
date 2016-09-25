package softwarementor.signup

import softwarementor.signup.UserConfirmation

interface UserConfirmationGateway {
    fun save(userConfirmation: UserConfirmation)

    fun findByCode(code: String): UserConfirmation?

}