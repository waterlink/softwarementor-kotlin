package softwarementor.fixtures

import softwarementor.login.Login

interface LoginFixture {
    val login: Login
    val executor: CommandExecutor

    @AcceptanceMethod
    fun whenLoggingInWithNameAndPassword(name: String, password: String): Boolean {
        executor.execute {
            login.login(name, password)
        }

        return true
    }

}

