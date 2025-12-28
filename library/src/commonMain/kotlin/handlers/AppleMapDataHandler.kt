package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.raise.ensure
import httpclient.createNoRedirectClient
import httpclient.getLocationHeader
import httpclient.getWithFullUA
import io.ktor.client.HttpClient

/**
 * Resolves Apple Maps links.
 * It extracts [LatLon] from the first redirect url
 */
class AppleMapDataHandler(
    private val httpClient: HttpClient = createNoRedirectClient(true),
) : MapDataHandler() {
    companion object {
        private const val APPLE_MAPS_URL_PREFIX = "https://maps.apple"
    }

    override fun canResolve(data: String): Boolean = data.startsWith(APPLE_MAPS_URL_PREFIX)

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        co.touchlab.kermit.Logger
            .i { "try extract from: $data" }
        val response = httpClient.getWithFullUA(data)
        val newUrl = response.getLocationHeader()
        co.touchlab.kermit.Logger
            .i { "apple newUrl: $newUrl" }
        return either {
            ensure(newUrl != null) { LatLonExtractError.Failed }
            newUrl.tryExtract(GENERAL_LAT_LON_REGEX).getOrElse { raise(LatLonExtractError.Failed) }
        }
    }
}
