package softwarementor.user

interface UserGateway {
    fun save(user: User)

    fun findByName(name: String): User?
}