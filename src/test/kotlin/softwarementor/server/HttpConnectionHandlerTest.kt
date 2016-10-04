package softwarementor.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.*

class HttpConnectionHandlerTest {
    private lateinit var connectionHandler: HttpConnectionHandler

    @Test
    fun canOutputSimpleResponse() {
        connectionHandler = HttpConnectionHandler {
            "<h1>hello world</h1>"
        }

        val response = makeRequest("GET / HTTP/1.1\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html\n" +
                        "Content-Length: 20\n" +
                        "\n" +
                        "<h1>hello world</h1>")
    }

    @Test
    fun canSeeGetRequestsMethod() {
        connectionHandler = HttpConnectionHandler {
            "method is: ${it.method}"
        }

        val response = makeRequest("GET / HTTP/1.1\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html\n" +
                        "Content-Length: 14\n" +
                        "\n" +
                        "method is: GET")
    }

    @Test
    fun canSeePostRequestsMethod() {
        connectionHandler = HttpConnectionHandler {
            "method is: ${it.method}"
        }

        val response = makeRequest("POST / HTTP/1.1\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html\n" +
                        "Content-Length: 15\n" +
                        "\n" +
                        "method is: POST")
    }

    @Test
    fun canSeeRequestsUrl() {
        connectionHandler = HttpConnectionHandler {
            "url is: ${it.url}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html\n" +
                        "Content-Length: 20\n" +
                        "\n" +
                        "url is: /hello/world")
    }

    @Test
    fun canSeeRequestsHttpVersion() {
        connectionHandler = HttpConnectionHandler {
            "http version is: ${it.httpVersion}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html\n" +
                        "Content-Length: 25\n" +
                        "\n" +
                        "http version is: HTTP/1.1")
    }

    @Test
    fun hasAccessToRequestsHeaders() {
        connectionHandler = HttpConnectionHandler {
            "user agent is: ${it.headers["User-Agent"]}\n" +
                    "and from is: ${it.headers["From"]}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\n" +
                "User-Agent: testSuite/1.0\n" +
                "From: john@example.org\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html\n" +
                        "Content-Length: 58\n" +
                        "\n" +
                        "user agent is: testSuite/1.0\n" +
                        "and from is: john@example.org")
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
        val (method, url, httpVersion) = requestLine.split(" ")
        val headers = mutableMapOf<String, String>()

        reader.forEachLine {
            val (header, value) = it.split(": ")
            headers[header] = value
        }

        val request = HttpRequest(method, url, httpVersion, headers)
        val responseBody = requestHandler(request)

        val writer = clientConnection.writer()
        writer.write("HTTP/1.1 200 OK\n" +
                "Content-Type: text/html\n" +
                "Content-Length: ${responseBody.length}\n" +
                "\n" +
                responseBody)
        writer.flush()
    }

}

class HttpRequest(
        val method: String,
        val url: String,
        val httpVersion: String,
        val headers: Map<String, String>
)
