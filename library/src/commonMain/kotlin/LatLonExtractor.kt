import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import handlers.GeneralMapDataHandler
import handlers.GoogleMapsMapDataHandler

sealed interface LatLonExtractError {
    data class GeneralError(val description: String) : LatLonExtractError
    data object Failed : LatLonExtractError
    data object ExceedRetriesAmount : LatLonExtractError
}

class LatLonExtractor(httpClientLogs: Boolean = false) {
    private val registeredHandlers = linkedSetOf(
        GoogleMapsMapDataHandler(httpClientLogs)
    )

    suspend fun extractFromStringData(data: String?): Either<LatLonExtractError, LatLon> {
        return either {
            ensure(data != null) { LatLonExtractError.GeneralError("No data") }
            val handler = registeredHandlers.firstOrNull { handler ->
                handler.canResolve(data)
            } ?: GeneralMapDataHandler()
            handler.resolve(data).bind()
        }
    }
}