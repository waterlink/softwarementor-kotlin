package softwarementor.mentor

class Mentor(val name: String, val email: String, val password: String, val language: String) {
    var id: String? = null
    var isConfirmed = false
}