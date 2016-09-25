package softwarementor.signup

import softwarementor.Context
import softwarementor.user.RoleCreator
import softwarementor.mentor.MentorRoleCreator
import softwarementor.signup.EmailConfirmationService
import softwarementor.user.User
import softwarementor.signup.UserConfirmation
import softwarementor.mentee.Mentee
import softwarementor.mentee.MenteeRoleCreator
import java.util.*

class SignUp(private val context: Context,
             private val emailConfirmationService: EmailConfirmationService) {

    fun signUpAsMentee(name: String, email: String, password: String) {
        signUp(name, email, password, MenteeRoleCreator(context))
    }

    fun signUpAsMentor(name: String, email: String, password: String) {
        signUp(name, email, password, MentorRoleCreator(context))
    }

    fun confirm(confirmationCode: String) {
        val userConfirmation = context.userConfirmationGateway.findByCode(confirmationCode)
                ?: throw InvalidConfirmationCode()

        val user = context.userGateway.findByName(userConfirmation.name)

        user?.isConfirmed = true
        context.userGateway.save(user!!)
    }

    private fun signUp(name: String, email: String, password: String, roleCreator: RoleCreator) {
        val user = context.userGateway.findByName(name)

        if (user != null)
            throw UserAlreadyRegistered()

        val newUser = User(name, email, password)
        context.userGateway.save(newUser)

        roleCreator.create(name)

        val code = UUID.randomUUID().toString()
        val userConfirmation = UserConfirmation(name, code)
        context.userConfirmationGateway.save(userConfirmation)

        emailConfirmationService.sendConfirmationEmail(email, name, code)
    }
}