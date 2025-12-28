package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.raise.ensure
import httpclient.createNoRedirectClient
import httpclient.getLocationHeader
import httpclient.getWithFullUA
import httpclient.getWithSimpleUA
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class GoogleMapsMapDataHandler(
    httpClientLogs: Boolean = false,
    private val httpClient: HttpClient = createNoRedirectClient(
        httpClientLogs
    )
) : MapDataHandler() {
    companion object {

        private const val GOOGLE_MAPS_URL_PREFIX = "https://maps.app.goo.gl/"

        private const val MAX_RETRIES = 5
        private val RETRY_DELAY = 1.seconds
        private val MAX_REDIRECTS = 2

        private val GOOGLE_LAT_LON_REGEX =
            Regex("""window\.APP_INITIALIZATION_STATE=\[\[\[-?\d+(?:\.\d+)?,\s*(-?\d+\.?\d*),\s*(-?\d+\.?\d*)""")
    }

    override fun canResolve(data: String): Boolean {
        return data.startsWith(GOOGLE_MAPS_URL_PREFIX)
    }

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        return internalResolve(data, 0)
    }

    private suspend fun internalResolve(
        data: String,
        att: Int
    ): Either<LatLonExtractError, LatLon> {
        var response: HttpResponse? = null
        var responseStatus = HttpStatusCode.NotFound
        var attempts = 0
        while (responseStatus == HttpStatusCode.NotFound && attempts < MAX_RETRIES) {
            if (attempts > 0) {
                co.touchlab.kermit.Logger.w { "Next attempt" }
            }
            response = httpClient.getWithSimpleUA(data)
            responseStatus = response.status
            co.touchlab.kermit.Logger.d { "responseStatus: $responseStatus" }

            attempts++
            delay(RETRY_DELAY)

        }

        return either {
            ensure(response != null) { raise(LatLonExtractError.ExceedRetriesAmount) }

            val newUrl = response.getLocationHeader()
            co.touchlab.kermit.Logger.d { "new url for extraction :  $newUrl" }
            if (att < MAX_REDIRECTS && newUrl != null) {
                return internalResolve(newUrl, att + 1)
            }
            extractLatLonFromUrlContent(newUrl ?: data).getOrElse {
                raise(LatLonExtractError.Failed)
            }
        }
    }

    private suspend fun extractLatLonFromUrlContent(
        url: String,
    ): Option<LatLon> {
        co.touchlab.kermit.Logger.i { "try extract from: $url" }
        val htmlContent: String = httpClient.getWithFullUA(url).body()
        return htmlContent.tryExtract(GOOGLE_LAT_LON_REGEX, latLonReversed = true)
    }
}