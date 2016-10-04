package softwarementor.server

import java.net.ServerSocket
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

class SocketServer(private val executorService: ExecutorService, port: Int) {
    private val server = ServerSocket(port)

    val localPort: Int
        get() = server.localPort

    fun start(connectionHandler: ConnectionHandler) {
        executorService.execute {
            whileReturnsTrue {
                handleNextConnection(connectionHandler)
            }
        }
    }

    fun start(function: (ClientConnection) -> Unit) {
        start(object : ConnectionHandler {
            override fun handle(clientConnection: ClientConnection) {
                function(clientConnection)
            }
        })
    }

    private fun handleNextConnection(connectionHandler: ConnectionHandler): Boolean {
        val clientConnection = acceptConnection() ?: return false

        executorService.execute {
            connectionHandler.handle(clientConnection)
        }

        return true
    }

    private fun acceptConnection(): ClientConnection? {
        try {
            return StockClientConnection(server.accept())
        } catch (socketException: SocketException) {
            System.err.printf("Got error: %s\n Shutting down\n", socketException)
            return null
        }
    }

    private fun whileReturnsTrue(function: () -> Boolean) {
        do {
        } while (function())
    }

    fun close() {
        executorService.shutdown()
        server.close()
        executorService.awaitTermination(2, TimeUnit.SECONDS)
    }

}