package softwarementor.mentor

class CurrentMentorRepository {
    var currentMentor: Mentor? = null

    fun assume(mentor: Mentor?) {
        currentMentor = mentor
    }

    fun assumeGuest() {
        currentMentor = null
    }
}