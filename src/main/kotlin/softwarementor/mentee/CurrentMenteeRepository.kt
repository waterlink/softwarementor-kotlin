package softwarementor.mentee

class CurrentMenteeRepository {
    internal var currentMentee: Mentee? = null

    fun assumeGuest() {
        currentMentee = null
    }

    fun assume(mentee: Mentee) {
        currentMentee = mentee
    }

    fun assumeMentee() {
        assume(Mentee("MenteeUserName", "exampleEmail@example.org", "NO_PASSWORD"))
    }

    fun isGuest() = currentMentee == null

    fun isLoggedIn() = currentMentee != null
}