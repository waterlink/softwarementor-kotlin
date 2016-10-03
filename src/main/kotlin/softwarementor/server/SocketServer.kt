package softwarementor.server

import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

class SocketServer(private val executorService: ExecutorService, port: Int) {
    private val server = ServerSocket(port)

    val localPort: Int
        get() = server.localPort

    fun start(connectionHandler: (Socket) -> Unit) {
        executorService.execute {
            whileReturnsTrue {
                handleNextConnection(connectionHandler)
            }
        }
    }

    private fun handleNextConnection(connectionHandler: (Socket) -> Unit): Boolean {
        val socket = acceptConnection() ?: return false

        executorService.execute {
            connectionHandler(socket)
        }

        return true
    }

    private fun acceptConnection(): Socket? {
        try {
            return server.accept()
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