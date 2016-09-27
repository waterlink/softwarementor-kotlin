package softwarementor.fixtures

class CommandExecutor(private val commandHandler: FailingCommandHandler) {

    fun execute(block: () -> Unit) {
        try {
            block()
            commandHandler.handleSuccess()
        } catch (exception: Exception) {
            commandHandler.handleError(exception)
        }
    }

    interface FailingCommandHandler {
        fun handleError(exception: Exception)
        fun handleSuccess()
    }

}