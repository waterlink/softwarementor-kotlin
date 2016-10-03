package softwarementor.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SocketServerTest {
    @Rule @JvmField val globalTimeout = Timeout.seconds(3)!!

    private val lock = Object()

    private var connectionCount = 0

    private val executorService = Executors.newFixedThreadPool(4)

    private lateinit var server: ServerSocket

    private var port: Int = -1

    @After
    fun tearDown() {
        executorService.shutdown()
        server.close()
        executorService.awaitTermination(2, TimeUnit.SECONDS)
    }

    @Test
    fun acceptsOneConnection() {
        startServer(withNotification { connectionCount++ })

        synchronized(lock) {
            Socket("localhost", port)
            waitForLock()
        }

        assertThat(connectionCount).isEqualTo(1)
    }

    @Test
    fun acceptsTwoConnections() {
        startServer(withNotification { connectionCount++ })

        synchronized(lock) {
            Socket("localhost", port)
            waitForLock()
        }

        synchronized(lock) {
            Socket("localhost", port)
            waitForLock()
        }

        assertThat(connectionCount).isEqualTo(2)
    }

    @Test
    fun echoServer() {
        startServer(withNotification {
            val message = it.inputStream.bufferedReader().readLine()
            val writer = it.outputStream.writer()
            writer.write(message + "\n")
            writer.flush()
        })

        val socket = Socket("localhost", port)
        val writer = socket.outputStream.bufferedWriter()
        writer.write("hello world\n")
        writer.flush()

        val message = socket.inputStream.bufferedReader().readLine()
        assertThat(message).isEqualTo("hello world")
    }

    private fun startServer(connectionHandler: (socket: Socket) -> Unit) {
        server = ServerSocket(0)
        port = server.localPort

        executorService.execute {
            while (true) {
                val socket: Socket

                try {
                    socket = server.accept()
                } catch (socketException: SocketException) {
                    System.err.printf("Got error: %s\n Shutting down\n", socketException)
                    break
                }

                executorService.execute {
                    connectionHandler(socket)
                }
            }
        }
    }

    private fun withNotification(connectionHandler: (socket: Socket) -> Unit): (socket: Socket) -> Unit {
        return {
            synchronized(lock) {
                connectionHandler(it)
                notifyLock()
            }
        }
    }

    private fun waitForLock() {
        println("gonna wait")
        lock.wait(2000)
    }

    private fun notifyLock() {
        println("gonna notify")
        lock.notify()
    }
}

