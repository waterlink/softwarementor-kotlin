package softwarementor.user

import softwarementor.user.User

interface UserGateway {
    fun save(user: User)

    fun findByName(name: String): User?
}