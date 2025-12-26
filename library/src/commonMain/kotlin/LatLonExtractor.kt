import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
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
)

sealed interface LatLonExtractError {
    data class GeneralError(val description: String) : LatLonExtractError
}

internal suspend fun extractFromStringData(data: String?): Either<LatLonExtractError, LatLon> {
    return either {
        ensure(data != null) { LatLonExtractError.GeneralError("No data") }
        val handler = registeredHandlers.firstOrNull { handler ->
            handler.canResolve(data)
        } ?: GeneralMapDataHandler(httpClient)
        handler.resolve(data).bind()
    }
}