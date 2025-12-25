import handlers.GeneralMapDataHandler
import handlers.GoogleMapsMapDataHandler

private val registeredHandlers = setOf(
    GoogleMapsMapDataHandler(),
    GeneralMapDataHandler()
)

internal suspend fun extractFromStringData(data: String?): LatLon? {
    if (data == null) {
        return null
    }
    return registeredHandlers.firstNotNullOfOrNull { handler ->
        handler.resolve(data)
    }
}