package softwarementor.server

interface ConnectionHandler {
    fun handle(clientConnection: ClientConnection)

}