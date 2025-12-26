import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers

internal suspend fun HttpClient.extractLatLonFromUrlContent(
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

private val latLonRegex =
    Regex("""window\.APP_INITIALIZATION_STATE=\[\[\[-?\d+(?:\.\d+)?,\s*(-?\d+\.?\d*),\s*(-?\d+\.?\d*)""")

private fun extractLatLonFromUrlContentInternal(htmlContent: String): Option<LatLon> {
    latLonRegex.find(htmlContent)?.let { match ->
        val val1 = match.groupValues[1].toDoubleOrNull()
        val val2 = match.groupValues[2].toDoubleOrNull()

        if (val1 != null && val2 != null) {
            return Some(LatLon(val1, val2))
        }
    }

    return None
}
