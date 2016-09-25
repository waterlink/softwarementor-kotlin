package softwarementor.user

class InMemoryUserGateway : UserGateway {
    private val entities: MutableMap<String, UserStoredInMemory> = mutableMapOf()

    override fun save(user: User) {
        entities[user.name] = UserStoredInMemory(user)
    }

    override fun findByName(name: String): User? {
        return entities[name]?.constructUser()
    }

    private class UserStoredInMemory(user: User) {
        val name = user.name
        val email = user.email
        val password = user.password
        val isConfirmed = user.isConfirmed

        fun constructUser() =
                User(name, email, password)
                        .withConfirmed(isConfirmed)

        fun User.withConfirmed(confirmed: Boolean): User {
            this.isConfirmed = confirmed
            return this
        }

    }

}
