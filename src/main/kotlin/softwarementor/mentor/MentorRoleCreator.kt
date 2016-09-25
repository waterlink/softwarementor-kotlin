package softwarementor.mentor

import softwarementor.Context
import softwarementor.user.RoleCreator

class MentorRoleCreator(private val context: Context) : RoleCreator {
    override fun create(name: String) {
        context.mentorGateway.save(Mentor(name, ""))
    }
}