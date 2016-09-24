package softwarementor.user

import softwarementor.user.Mentee
import softwarementor.user.User

class CurrentUserRepository {
    private var currentUser: User? = null

    fun assumeGuest() {
        currentUser = null
    }

    fun isGuest() = currentUser == null

    fun assumeMentee() {
        currentUser = Mentee()
    }

    fun isMentee() = currentUser != null

}