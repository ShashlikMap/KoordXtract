package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

class AppleMapDataHandler(private val httpClient: HttpClient = createInternalClient(true)) :
    MapDataHandler() {
    companion object {
        private const val APPLE_MAPS_URL_PREFIX = "https://maps.apple"

        private fun createInternalClient(httpClientLogs: Boolean) = HttpClient {
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

        private val LAT_LON_REGEX =
            Regex("([-+]?\\d*\\.?\\d+),\\s*([-+]?\\d*\\.?\\d+)")
    }

    override fun canResolve(data: String): Boolean {
        return data.startsWith(APPLE_MAPS_URL_PREFIX)
    }

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        co.touchlab.kermit.Logger.i { "try extract from: $data" }
        val response = httpClient.getWithFullUA(data)
        val newUrl = response.headers["Location"]
        co.touchlab.kermit.Logger.i { "apple newUrl: $newUrl" }
        if (newUrl != null) {
            LAT_LON_REGEX.find(newUrl)?.let { match ->
                val val1 = match.groupValues[1].toDoubleOrNull()
                val val2 = match.groupValues[2].toDoubleOrNull()
                if (val1 != null && val2 != null) {
                    return LatLon(val1, val2).right()
                }
            }

        }
        return LatLonExtractError.Failed.left()
    }
}