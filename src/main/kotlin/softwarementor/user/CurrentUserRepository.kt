package softwarementor.user

import softwarementor.user.User

class CurrentUserRepository {
    var currentUser: User? = null

    fun assume(user: User?) {
        currentUser = user
    }

    fun assumeGuest() {
        currentUser = null
    }

}