import handlers.GeneralMapDataHandler
import handlers.GoogleMapsMapDataHandler
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

private val httpClient = HttpClient {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                co.touchlab.kermit.Logger.i { "HTTP Client:  $message" }
            }
        }
        level = LogLevel.HEADERS
    }
}

private val registeredHandlers = setOf(
    GoogleMapsMapDataHandler(httpClient),
    GeneralMapDataHandler(httpClient)
)

internal suspend fun extractFromStringData(data: String?): LatLon? {
    if (data == null) {
        return null
    }
    return registeredHandlers.firstNotNullOfOrNull { handler ->
        handler.resolve(data)
    }
}