package softwarementor.signup

class InMemoryUserConfirmationGateway : UserConfirmationGateway {
    private val entities: MutableMap<String, UserConfirmationStoredInMemory> = mutableMapOf()

    override fun save(userConfirmation: UserConfirmation) {
        entities[userConfirmation.code] = UserConfirmationStoredInMemory(userConfirmation)
    }

    override fun findByCode(code: String) =
            entities[code]?.constructUserConfirmation()

    class UserConfirmationStoredInMemory(userConfirmation: UserConfirmation) {
        val name = userConfirmation.name
        val code = userConfirmation.code

        fun constructUserConfirmation(): UserConfirmation {
            return UserConfirmation(name, code)
        }

    }

}
