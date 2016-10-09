package softwarementor.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.*

class HttpConnectionHandlerTest {
    private lateinit var connectionHandler: HttpConnectionHandler

    @Test
    fun `outputs simple response`() {
        connectionHandler = HttpConnectionHandler {
            "<h1>hello world</h1>"
        }

        val response = makeRequest("GET / HTTP/1.1\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 20\r\n" +
                        "\r\n" +
                        "<h1>hello world</h1>")
    }

    @Test
    fun `provides get request method`() {
        connectionHandler = HttpConnectionHandler {
            "method is: ${it.method}"
        }

        val response = makeRequest("GET / HTTP/1.1\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 14\r\n" +
                        "\r\n" +
                        "method is: GET")
    }

    @Test
    fun `provides post request method`() {
        connectionHandler = HttpConnectionHandler {
            "method is: ${it.method}"
        }

        val response = makeRequest("POST / HTTP/1.1\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 15\r\n" +
                        "\r\n" +
                        "method is: POST")
    }

    @Test
    fun `provides request url`() {
        connectionHandler = HttpConnectionHandler {
            "url is: ${it.url}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 20\r\n" +
                        "\r\n" +
                        "url is: /hello/world")
    }

    @Test
    fun `provides http version`() {
        connectionHandler = HttpConnectionHandler {
            "http version is: ${it.httpVersion}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 25\r\n" +
                        "\r\n" +
                        "http version is: HTTP/1.1")
    }

    @Test
    fun `splits request line by space and tabs`() {
        connectionHandler = HttpConnectionHandler {
            "method: ${it.method}, url: ${it.url}, version: ${it.httpVersion}"
        }

        val response = makeRequest("GET \t\t  /hello/world\t\t \t  HTTP/1.1\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 49\r\n" +
                        "\r\n" +
                        "method: GET, url: /hello/world, version: HTTP/1.1")
    }

    @Test
    fun `provides headers`() {
        connectionHandler = HttpConnectionHandler {
            "user agent is: ${it.headers["User-Agent"]}\n" +
                    "and from is: ${it.headers["From"]}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n" +
                "User-Agent: testSuite/1.0\r\n" +
                "From: john@example.org\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 58\r\n" +
                        "\r\n" +
                        "user agent is: testSuite/1.0\n" +
                        "and from is: john@example.org")
    }

    @Test
    fun `provides headers without CR`() {
        connectionHandler = HttpConnectionHandler {
            "user agent is: ${it.headers["User-Agent"]}\n" +
                    "and from is: ${it.headers["From"]}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\n" +
                "User-Agent: testSuite/1.0\n" +
                "From: john@example.org\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 58\r\n" +
                        "\r\n" +
                        "user agent is: testSuite/1.0\n" +
                        "and from is: john@example.org")
    }

    @Test
    fun `provides multiline headers`() {
        connectionHandler = HttpConnectionHandler {
            "user agent is: ${it.headers["User-Agent"]}\n" +
                    "and from is: ${it.headers["From"]}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n" +
                "User-Agent:   testSuite/1.0,\r\n" +
                " that is having a weird format\r\n" +
                "\t   \t and some whitespace to the mix\r\n" +
                "From:  \t \t john@example.org\r\n" +
                "\t(John Smith)\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 133\r\n" +
                        "\r\n" +
                        "user agent is: testSuite/1.0, that is having a weird format and some whitespace to the mix\n" +
                        "and from is: john@example.org (John Smith)")
    }

    private fun makeRequest(input: String): String {
        val reader = BufferedReader(InputStreamReader(input.byteInputStream()))
        val outputStream = ByteArrayOutputStream()
        val writer = BufferedWriter(OutputStreamWriter(outputStream))

        connectionHandler.handle(object : ClientConnection {
            override fun reader() = reader
            override fun writer() = writer
        })

        return outputStream.toString("utf-8")
    }
}

class HttpConnectionHandler(private val requestHandler: (HttpRequest) -> String) : ConnectionHandler {
    override fun handle(clientConnection: ClientConnection) {
        val reader = clientConnection.reader()
        val requestLine = reader.readLine()
        val (method, url, httpVersion) = splitRequestLine(requestLine)
        val headers = gatherHeadersFrom(reader)

        val request = HttpRequest(method, url, httpVersion, headers)
        val responseBody = requestHandler(request)

        val writer = clientConnection.writer()
        writer.write("HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: ${responseBody.length}\r\n" +
                "\r\n" +
                responseBody)
        writer.flush()
    }

    private fun splitRequestLine(requestLine: String) =
            requestLine.split(Regex("[ \t]")).filter { it != "" }

    private fun gatherHeadersFrom(reader: BufferedReader): MutableMap<String, String> {
        val headers: MutableMap<String, String> = mutableMapOf<String, String>()
        var previousHeader: String? = null

        reader.forEachLine {
            if (it.first().isWhitespace()) {
                headers[previousHeader!!] = headers[previousHeader!!] + " " + it.trimStart()
            } else {
                val (header, value) = it.split(": ")
                headers[header] = value.trimStart()
                previousHeader = header
            }
        }
        return headers
    }

}

class HttpRequest(
        val method: String,
        val url: String,
        val httpVersion: String,
        val headers: Map<String, String>
)
