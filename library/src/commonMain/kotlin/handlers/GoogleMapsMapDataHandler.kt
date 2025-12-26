package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.core.raise.either
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay

class GoogleMapsMapDataHandler : MapDataHandler() {

    private val latLonRegex =
        Regex("""window\.APP_INITIALIZATION_STATE=\[\[\[-?\d+(?:\.\d+)?,\s*(-?\d+\.?\d*),\s*(-?\d+\.?\d*)""")


    private fun createClient() = HttpClient {
        followRedirects = false
        engine {
            followRedirects = false
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.i { "HTTP Client: $message" }
                }
            }
            level = LogLevel.HEADERS
        }
    }

    override fun canResolve(data: String): Boolean {
        return data.startsWith("https://maps.app.goo.gl/")
    }

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        var response = createClient().get(data) {
            headers {
                append("User-Agent", "Mozilla/5.0")
            }
        }

        co.touchlab.kermit.Logger.i { "response :  $response" }
        var responseCode = response.status
        while (responseCode == HttpStatusCode.NotFound) {
            delay(1000)
            co.touchlab.kermit.Logger.w { "NEXT ATTEMPT" }

            response = createClient().get(data) {
                headers {
                    append("User-Agent", "Mozilla/5.0")
                }
            }
            responseCode = response.status
        }

        val newUrl = response.headers["Location"]
        co.touchlab.kermit.Logger.i { "newUrl :  $newUrl" }
        return either {
            extractLatLonFromUrlContent(newUrl ?: data).getOrElse {
                raise(LatLonExtractError.GeneralError("No data"))
            }
        }
    }

    private suspend fun extractLatLonFromUrlContent(
        url: String,
    ): Option<LatLon> {
        co.touchlab.kermit.Logger.i { "extract from :  $url" }
        val htmlContent: String = HttpClient().get(url) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                )
            }
        }.body()

        return extractLatLonFromUrlContentInternal(htmlContent)
    }

    // TODO Make private
    fun extractLatLonFromUrlContentInternal(htmlContent: String): Option<LatLon> {
        latLonRegex.find(htmlContent)?.let { match ->
            val val1 = match.groupValues[1].toDoubleOrNull()
            val val2 = match.groupValues[2].toDoubleOrNull()

            if (val1 != null && val2 != null) {
                return Some(LatLon(val1, val2))
            }
        }

        return None
    }
}