package handlers

import LatLon
import extractLatLonFromUrlContent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.utils.io.CancellationException
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

    suspend fun resolve(data: String): LatLon? {
        if (!canResolve(data)) return null
        try {
            var latLon = httpClient.extractLatLonFromUrlContent(data)
            if (latLon != null) {
                return latLon
            }

            var response = createClient().get(data) {
                headers {
                    append("User-Agent", "Mozilla/5.0")
                }
            }

            var responseCode = response.status.value
            while (responseCode == 404) {
                delay(1000)
                co.touchlab.kermit.Logger.w { "KIOL NEXT ATTEMPT" }

                response = createClient().get(data) {
                    headers {
                        append("User-Agent", "Mozilla/5.0")
                    }
                }
                responseCode = response.status.value
            }

            val newUrl = response.headers["Location"]
            latLon = httpClient.extractLatLonFromUrlContent(newUrl!!)
            return latLon
        } catch (e: CancellationException) {
            throw e
        } catch (t: Throwable) {
            co.touchlab.kermit.Logger.e { "Can't resolve url: $t" }
            return null
        }
    }

    protected abstract fun canResolve(data: String): Boolean
}