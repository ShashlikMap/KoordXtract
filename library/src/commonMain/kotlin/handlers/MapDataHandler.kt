package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import extractLatLonFromUrlContent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import kotlinx.coroutines.delay

abstract class MapDataHandler(private val httpClient: HttpClient) {
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

    suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        var response = createClient().get(data) {
            headers {
                append("User-Agent", "Mozilla/5.0")
            }
        }

        co.touchlab.kermit.Logger.i { "response :  $response" }
        var responseCode = response.status.value
        while (responseCode == 404) {
            delay(1000)
            co.touchlab.kermit.Logger.w { "NEXT ATTEMPT" }

            response = createClient().get(data) {
                headers {
                    append("User-Agent", "Mozilla/5.0")
                }
            }
            responseCode = response.status.value
        }

        val newUrl = response.headers["Location"]
        co.touchlab.kermit.Logger.i { "newUrl :  $newUrl" }
        return either {
            httpClient.extractLatLonFromUrlContent(newUrl ?: data).getOrElse {
                raise(LatLonExtractError.GeneralError("No data"))
            }
        }
    }

    abstract fun canResolve(data: String): Boolean
}