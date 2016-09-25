package softwarementor.mentee

import softwarementor.Context
import softwarementor.user.RoleCreator

class MenteeRoleCreator : RoleCreator {
    override fun create(name: String) {
        Context.menteeGateway.save(Mentee(name))
    }
}