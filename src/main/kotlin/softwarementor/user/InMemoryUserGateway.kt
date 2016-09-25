package softwarementor.user

class InMemoryUserGateway : UserGateway {
    private val entities: MutableList<UserStoredInMemory> = mutableListOf()

    override fun save(user: User) {
        if (user.isNew) {
            entities.add(UserStoredInMemory(user))
            user.isNew = false
        } else {
            val index = entities.indexOfFirst { it.name == user.name }
            entities[index] = UserStoredInMemory(user)
        }
    }

    override fun findByName(name: String): User? {
        return entities.find { it.name == name }?.constructUser()
    }

    private class UserStoredInMemory(user: User) {
        val name = user.name
        val email = user.email
        val password = user.password
        val isConfirmed = user.isConfirmed

        fun constructUser() =
                User(name, email, password)
                        .withConfirmed(isConfirmed)
                        .andFromGateway()

        fun User.withConfirmed(confirmed: Boolean): User {
            this.isConfirmed = confirmed
            return this
        }

        fun User.andFromGateway(): User {
            this.isNew = false
            return this
        }

    }

}
