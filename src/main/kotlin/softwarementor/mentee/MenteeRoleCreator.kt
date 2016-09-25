package softwarementor.mentee

import softwarementor.Context
import softwarementor.user.RoleCreator

class MenteeRoleCreator(private val context: Context) : RoleCreator {

    override fun create(name: String) {
        context.menteeGateway.save(Mentee(name))
    }
}