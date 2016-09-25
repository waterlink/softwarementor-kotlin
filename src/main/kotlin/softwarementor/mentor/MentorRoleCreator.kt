package softwarementor.mentor

import softwarementor.Context
import softwarementor.user.RoleCreator

class MentorRoleCreator : RoleCreator {
    override fun create(name: String) {
        Context.mentorGateway.save(Mentor(name, ""))
    }
}