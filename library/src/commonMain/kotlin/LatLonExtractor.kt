import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import handlers.AppleMapDataHandler
import handlers.GeneralMapDataHandler
import handlers.GoogleMapsMapDataHandler

sealed interface LatLonExtractError {
    /**
     * No any data presented
     */
    data object NoData : LatLonExtractError

    /**
     * [LatLon] can't be extracted
     */
    data object Failed : LatLonExtractError

    /**
     * Too many attempts
     */
    data object ExceedRetriesAmount : LatLonExtractError
}

/**
 * Class to extracts [LatLon] for maps application.
 * Google and Apple Maps are supported.
 */
class LatLonExtractor(
    httpClientLogs: Boolean = false,
) {
    private val registeredHandlers =
        linkedSetOf(
            GoogleMapsMapDataHandler(httpClientLogs),
            AppleMapDataHandler(),
        )

    /**
     * Extracts [LatLon] from [data] or return [LatLonExtractError]
     */
    suspend fun extractFromStringData(data: String?): Either<LatLonExtractError, LatLon> {
        co.touchlab.kermit.Logger
            .d { "Trying to extract from: $data" }
        return either {
            ensure(data != null) { LatLonExtractError.NoData }
            val handler =
                registeredHandlers.firstOrNull { handler ->
                    handler.canResolve(data)
                } ?: GeneralMapDataHandler()
            handler.resolve(data).bind()
        }
    }
}
