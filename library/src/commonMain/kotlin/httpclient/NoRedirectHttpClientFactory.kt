package httpclient

import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers

/**
 * Creates [HttpClient] without redirects.
 * [httpClientLogs] enables [LogLevel.HEADERS] HTTP logs
 */
internal fun createNoRedirectClient(httpClientLogs: Boolean) = HttpClient {
    followRedirects = false
    engine {
        followRedirects = false
    }
    if (httpClientLogs) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.i { "HTTP Client: $message" }
                }
            }
            level = LogLevel.HEADERS
        }
    }
}

