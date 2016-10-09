package softwarementor.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.*
import java.net.URI
import java.nio.CharBuffer

class HttpConnectionHandlerTest {
    private lateinit var connectionHandler: HttpConnectionHandler

    @Test
    fun `outputs simple response`() {
        connectionHandler = HttpConnectionHandler {
            "<h1>hello world</h1>"
        }

        val response = makeRequest("GET / HTTP/1.1\r\n" +
                "Host: example.org\r\n")

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

        val response = makeRequest("GET / HTTP/1.1\r\n" +
                "Host: example.org\r\n")

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

        val response = makeRequest("POST / HTTP/1.1\r\n" +
                "Host: example.org\r\n")

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
            "url is: ${it.url}, path is: ${it.path}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n" +
                "Host: example.org\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 43\r\n" +
                        "\r\n" +
                        "url is: /hello/world, path is: /hello/world")
    }

    @Test
    fun `accepts absolute url`() {
        connectionHandler = HttpConnectionHandler {
            "url is: ${it.url}\n" +
                    "path is: ${it.path}\n" +
                    "host is: ${it.host}"
        }

        val response = makeRequest("GET http://example.org/the/path HTTP/1.1\r\n" +
                "Host: example.org\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 75\r\n" +
                        "\r\n" +
                        "url is: http://example.org/the/path\n" +
                        "path is: /the/path\n" +
                        "host is: example.org")
    }

    @Test
    fun `provides query`() {
        connectionHandler = HttpConnectionHandler {
            "query is: ${it.query}"
        }

        val response = makeRequest("GET /hello?name=world&greeting=hi HTTP/1.1\r\n" +
                "Host: example.org\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 32\r\n" +
                        "\r\n" +
                        "query is: name=world&greeting=hi")
    }

    @Test
    fun `provides http version`() {
        connectionHandler = HttpConnectionHandler {
            "http version is: ${it.httpVersion}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n" +
                "Host: example.org\r\n")

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

        val response = makeRequest("GET \t\t  /hello/world\t\t \t  HTTP/1.1\r\n" +
                "Host: example.org\r\n")

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
                "Host: example.org\r\n" +
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
    fun `provides headers with case-insensitivity`() {
        connectionHandler = HttpConnectionHandler {
            "user agent is: ${it.headers["uSEr-AGenT"]}\n" +
                    "and from is: ${it.headers["FROm"]}"
        }

        val response = makeRequest("GET /hello/world HTTP/1.1\r\n" +
                "HoST: example.org\r\n" +
                "USER-AgenT: testSuite/1.0\r\n" +
                "frOM: john@example.org\r\n")

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
                "Host: example.org\n" +
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
                "Host: example.org\r\n" +
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

    @Test
    fun `provides duplicated headers as list`() {
        connectionHandler = HttpConnectionHandler {
            "x-forwarded-for: ${it.headers["X-Forwarded-For"]}"
        }

        val response = makeRequest("GET / HTTP/1.1\r\n" +
                "Host: example.org\r\n" +
                "X-Forwarded-For: 1.2.3.4\r\n" +
                "x-forWARDEd-foR: 5.6.7.8\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 33\r\n" +
                        "\r\n" +
                        "x-forwarded-for: 1.2.3.4, 5.6.7.8")
    }

    @Test
    fun `provides request body`() {
        connectionHandler = HttpConnectionHandler {
            "body: ${it.body}"
        }

        val response = makeRequest("POST / HTTP/1.1\r\n" +
                "Host: example.org\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 27\r\n" +
                "\r\n" +
                "hello world\n" +
                "hello everyone!")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 33\r\n" +
                        "\r\n" +
                        "body: hello world\n" +
                        "hello everyone!")
    }

    @Test
    fun `can read chunked request`() {
        connectionHandler = HttpConnectionHandler {
            "body: ${it.body}\n" +
                    "a-footer: ${it.footers["a-footer"]}\n" +
                    "b-footer: ${it.footers["b-footer"]}"
        }

        val response = makeRequest("GET / HTTP/1.1\r\n" +
                "Host: example.org\r\n" +
                "Content-Type: text/plain\r\n" +
                "Transfer-Encoding: chunked\r\n" +
                "\r\n" +
                "1a; ignore stuff\r\n" +
                "abcdefghijkl\r\nopqrstuvwxyz\r\n" +
                "11; more stuff\r\n" +
                "1234567890abcdef_\r\n" +
                "0b\r\n" +
                "hello world\r\n" +
                "0\r\n" +
                "a-footer: a-value\r\n" +
                "b-footer: b-value\r\n" +
                "\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 96\r\n" +
                        "\r\n" +
                        "body: abcdefghijkl\r\nopqrstuvwxyz1234567890abcdef_hello world\n" +
                        "a-footer: a-value\n" +
                        "b-footer: b-value")
    }

    @Test
    fun `fails for empty request`() {
        connectionHandler = HttpConnectionHandler { "ok" }

        val response = makeRequest("")

        assertThat(response)
                .isEqualTo("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 34\r\n" +
                        "\r\n" +
                        "HTTP request line can not be empty")
    }

    @Test
    fun `fails for invalid request line`() {
        connectionHandler = HttpConnectionHandler { "ok" }

        val response = makeRequest("GET\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 72\r\n" +
                        "\r\n" +
                        "HTTP request line has to be in the format '{method} {url} {httpVersion}'")
    }

    @Test
    fun `fails for invalid headers format`() {
        connectionHandler = HttpConnectionHandler { "ok" }

        val response = makeRequest("GET / HTTP/1.1\r\n" +
                "hello world\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 26\r\n" +
                        "\r\n" +
                        "HTTP headers are malformed")
    }

    @Test
    fun `fails for incorrectly folded headers`() {
        connectionHandler = HttpConnectionHandler { "ok" }

        val response = makeRequest("GET / HTTP/1.1\r\n" +
                "\t  hello world\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 35\r\n" +
                        "\r\n" +
                        "HTTP headers are incorrectly folded")
    }

    @Test
    fun `fails if request body length do not match header`() {
        connectionHandler = HttpConnectionHandler { "ok" }

        val response = makeRequest("POST / HTTP/1.1\r\n" +
                "Host: example.org\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 254\r\n" +
                "\r\n" +
                "hello world\n" +
                "hello everyone!")

        assertThat(response)
                .isEqualTo("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 52\r\n" +
                        "\r\n" +
                        "HTTP request body length has to match Content-Length")
    }

    @Test
    fun `fails without Host header`() {
        connectionHandler = HttpConnectionHandler { "ok" }

        val response = makeRequest("GET / HTTP/1.1\r\n")

        assertThat(response)
                .isEqualTo("HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 39\r\n" +
                        "\r\n" +
                        "HTTP request has to specify Host header")
    }

    @Test
    fun TODO_CHECK_ALL_TO_INTS() {
    }

    @Test
    fun TODO_CHECK_DISCARDED_CRLF() {
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
        val writer = clientConnection.writer()

        try {
            handleRequest(clientConnection, writer)
        } catch(badRequest: BadRequestError) {
            respondWithBadRequest(badRequest, writer)
        }

    }

    private fun handleRequest(clientConnection: ClientConnection, writer: Writer) {
        val request = parseRequest(clientConnection)
        val responseBody = requestHandler(request)

        writer.write("HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: ${responseBody.length}\r\n" +
                "\r\n" +
                responseBody)
        writer.flush()
    }

    private fun parseRequest(clientConnection: ClientConnection): HttpRequest {
        val reader = clientConnection.reader()
        val requestLine = reader.readLine()
        val (method, url, httpVersion) = splitRequestLine(requestLine)
        val uri = URI(url)
        val path = uri.path
        val query = uri.query
        val headers = parseHeadersOrFooters(reader)
        val host = getHost(headers)
        val body = parseBody(headers, reader)
        val footers = parseHeadersOrFooters(reader)
        return HttpRequest(method, url, path, query, httpVersion, headers, host, body, footers)
    }

    private fun getHost(headers: MutableMap<String, String>): String {
        return headers["Host"] ?: throw BadRequestError("HTTP request has to specify Host header")
    }

    private fun parseBody(headers: MutableMap<String, String>, reader: BufferedReader): String {
        val transferEncoding = headers["Transfer-Encoding"] ?: "identity"

        if (transferEncoding == "chunked") {

            var body = ""

            while (true) {
                val line: String? = reader.readLine()

                if (line == null || line.isEmpty()) break

                val contentLengthHex = line.split(';').first()
                val contentLength = Integer.parseInt(contentLengthHex, 16)

                if (contentLength == 0) break

                val buffer = CharArray(contentLength)
                reader.read(buffer, 0, contentLength)

                val discard = CharArray(2)
                reader.read(discard, 0, 2)

                body += buffer.joinToString("")
            }

            return body

        } else {
            val body = reader.readText()

            val contentLength = headers["Content-Length"]
            if (contentLength != null && contentLength.toInt() != body.length) {
                throw BadRequestError("HTTP request body length has to match Content-Length")
            }

            return body
        }
    }

    private fun splitRequestLine(requestLine: String?): List<String> {
        if (requestLine == null)
            throw BadRequestError("HTTP request line can not be empty")

        val components = requestLine.split(Regex("[ \t]+"))

        if (components.size != 3)
            throw BadRequestError("HTTP request line has to be in the format '{method} {url} {httpVersion}'")

        return components
    }

    private fun parseHeadersOrFooters(reader: BufferedReader): MutableMap<String, String> {
        val headers = Headers()
        var previousHeader: String? = null

        while (true) {
            val line: String? = reader.readLine()

            if (line == null || line.isEmpty()) break

            if (line.first().isWhitespace()) {
                unfoldHeader(headers, previousHeader, line)
            } else {
                val (header, value) = splitHeaderLine(line)
                recordHeader(headers, header, value)
                previousHeader = header
            }
        }

        return headers
    }

    private fun recordHeader(headers: Headers, header: String, value: String) {
        val previousValue = headers[header]?.plus(", ") ?: ""
        headers[header] = previousValue + value.trimStart()
    }

    private fun unfoldHeader(headers: MutableMap<String, String>, previousHeader: String?, line: String) {
        if (previousHeader == null)
            throw BadRequestError("HTTP headers are incorrectly folded")

        headers[previousHeader] = headers[previousHeader] + " " + line.trimStart()
    }

    private fun splitHeaderLine(line: String): List<String> {
        val components = line.split(": ")

        if (components.size != 2)
            throw BadRequestError("HTTP headers are malformed")

        return components
    }

    private fun respondWithBadRequest(badRequest: BadRequestError, writer: Writer) {
        val contentLength = badRequest.message?.length ?: 0

        writer.write("HTTP/1.1 400 Bad Request\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: $contentLength\r\n" +
                "\r\n" +
                badRequest.message)

        writer.flush()
    }

}

class Headers(private val map: MutableMap<String, String>) :
        MutableMap<String, String> by map {

    constructor() : this(mutableMapOf())

    override fun get(key: String) =
            map[key.toLowerCase()]

    override fun put(key: String, value: String) =
            map.put(key.toLowerCase(), value)

}

class BadRequestError(message: String) : Exception(message) {

}

class HttpRequest(
        val method: String,
        val url: String,
        val path: String,
        val query: String?,
        val httpVersion: String,
        val headers: Map<String, String>,
        val host: String,
        val body: String,
        val footers: Map<String, String>
)
