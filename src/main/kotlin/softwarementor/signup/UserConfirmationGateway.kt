package softwarementor.signup

interface UserConfirmationGateway {
    fun save(userConfirmation: UserConfirmation)

    fun findByCode(code: String): UserConfirmation?

}