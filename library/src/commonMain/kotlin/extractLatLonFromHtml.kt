import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers

internal suspend fun HttpClient.extractLatLonFromUrlContent(
    url: String,
): LatLon? {
    try {
        val htmlContent: String = HttpClient().get(url) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                )
            }
        }.body()

        close()

        return extractLatLonFromUrlContentInternal(htmlContent)
    } catch (e: Exception) {
        co.touchlab.kermit.Logger.e { "Exception: $e" }
    }

    return null
}

private val latLonRegex =
    Regex("""window\.APP_INITIALIZATION_STATE=\[\[\[-?\d+(?:\.\d+)?,\s*(-?\d+\.?\d*),\s*(-?\d+\.?\d*)""")

private fun extractLatLonFromUrlContentInternal(htmlContent: String): LatLon? {
    latLonRegex.find(htmlContent)?.let { match ->
        val val1 = match.groupValues[1].toDoubleOrNull()
        val val2 = match.groupValues[2].toDoubleOrNull()

        if (val1 != null && val2 != null) {
            return LatLon(val2, val1) // Return as (lat, lon)
        }
    }

    return null
}
