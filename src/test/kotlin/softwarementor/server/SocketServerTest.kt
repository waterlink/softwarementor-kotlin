package softwarementor.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import java.net.Socket
import java.util.concurrent.Executors

class SocketServerTest {
    @Rule @JvmField val globalTimeout = Timeout.seconds(3)!!

    private val lock = Object()

    private var connectionCount = 0

    private val executorService = Executors.newFixedThreadPool(4)

    private lateinit var server: SocketServer

    private val RANDOM_PORT = 0
    private var port = RANDOM_PORT

    @Before
    fun setUp() {
        server = SocketServer(executorService, port)
        port = server.localPort
    }

    @After
    fun tearDown() = server.close()

    @Test
    fun acceptsOneConnection() {
        server.start(withNotification { connectionCount++ })

        synchronized(lock) {
            Socket("localhost", port)
            waitForLock()
        }

        assertThat(connectionCount).isEqualTo(1)
    }

    @Test
    fun acceptsTwoConnections() {
        server.start(withNotification { connectionCount++ })

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
        server.start {
            val message = it.reader().readLine()
            val writer = it.writer()
            writer.write(message + "\n")
            writer.flush()
        }

        val socket = Socket("localhost", port)
        val writer = socket.outputStream.bufferedWriter()
        writer.write("hello world\n")
        writer.flush()

        val message = socket.inputStream.bufferedReader().readLine()
        assertThat(message).isEqualTo("hello world")
    }

    private fun withNotification(connectionHandler: (clientConnection: ClientConnection) -> Unit):
            (clientConnection: ClientConnection) -> Unit {
        return {
            synchronized(lock) {
                connectionHandler(it)
                notifyLock()
            }
        }
    }

    private fun waitForLock() {
        lock.wait(2000)
    }

    private fun notifyLock() {
        lock.notify()
    }
}

