package softwarementor.server

import java.io.BufferedReader
import java.io.Writer

interface ClientConnection {
    fun reader(): BufferedReader

    fun writer(): Writer

}