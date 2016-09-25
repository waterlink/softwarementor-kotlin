package softwarementor.signup

import softwarementor.Context
import softwarementor.user.RoleCreator
import softwarementor.mentor.MentorRoleCreator
import softwarementor.signup.EmailConfirmationService
import softwarementor.user.User
import softwarementor.signup.UserConfirmation
import softwarementor.mentee.Mentee
import java.util.*

class SignUp(private val emailConfirmationService: EmailConfirmationService) {

    fun signUp(name: String, email: String, password: String, roleCreator: RoleCreator) {
        val user = Context.userGateway.findByName(name)

        if (user != null)
            throw UserAlreadyRegistered()

        val newUser = User(name, email, password)
        Context.userGateway.save(newUser)

        roleCreator.create(name)

        val code = UUID.randomUUID().toString()
        val userConfirmation = UserConfirmation(name, code)
        Context.userConfirmationGateway.save(userConfirmation)

        emailConfirmationService.sendConfirmationEmail(email, name, code)
    }

    fun confirm(confirmationCode: String) {
        val userConfirmation = Context.userConfirmationGateway.findByCode(confirmationCode)
                ?: throw InvalidConfirmationCode()

        val user = Context.userGateway.findByName(userConfirmation.name)
        user?.isConfirmed = true
    }
}