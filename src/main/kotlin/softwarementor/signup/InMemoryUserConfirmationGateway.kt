package softwarementor.signup

import softwarementor.signup.UserConfirmation
import softwarementor.signup.UserConfirmationGateway

class InMemoryUserConfirmationGateway : UserConfirmationGateway {
    private val entities: MutableList<UserConfirmation> = mutableListOf()

    override fun save(userConfirmation: UserConfirmation) {
        entities.add(userConfirmation)
    }

    override fun findByCode(code: String) =
            entities.find { it.code == code }

}