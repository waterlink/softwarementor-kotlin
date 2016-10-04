package softwarementor.server

import java.net.Socket

class StockClientConnection(private val socket: Socket) : ClientConnection {
    override fun reader() = socket.inputStream.bufferedReader()

    override fun writer() = socket.outputStream.writer()

}