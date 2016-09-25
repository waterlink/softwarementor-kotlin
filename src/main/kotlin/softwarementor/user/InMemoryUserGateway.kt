package softwarementor.user

import softwarementor.user.User

class InMemoryUserGateway : UserGateway {
    private val users: MutableList<User> = mutableListOf()

    override fun save(user: User) {
        users.add(user)
    }

    override fun findByName(name: String): User? {
        return users.find { it.name == name }
    }

}