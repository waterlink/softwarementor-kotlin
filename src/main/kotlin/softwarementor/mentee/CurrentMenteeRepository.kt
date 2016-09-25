package softwarementor.mentee

class CurrentMenteeRepository {
    internal var currentMentee: Mentee? = null

    fun assumeGuest() {
        currentMentee = null
    }

    fun assume(mentee: Mentee?) {
        currentMentee = mentee
    }

    fun isGuest() = currentMentee == null
}