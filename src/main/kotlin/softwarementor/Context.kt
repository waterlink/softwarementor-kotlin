package softwarementor

import softwarementor.user.CurrentUserRepository
import softwarementor.Gateway

object Context {
    lateinit var gateway: Gateway
    lateinit var currentUserRepository: CurrentUserRepository
}