package httpclient

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse

/**
 * Executes request with a simple user-agent
 */
internal suspend fun HttpClient.getWithSimpleUA(url: String) = get(url) {
    headers {
        append("User-Agent", "Mozilla/5.0")
    }
}

/**
 * Executes request with a full user-agent
 */
internal suspend fun HttpClient.getWithFullUA(url: String) = get(url) {
    headers {
        append(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        )
    }
}

/**
 * Returns Location header
 */
internal fun HttpResponse?.getLocationHeader() = this?.headers["Location"]