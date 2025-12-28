package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers

abstract class MapDataHandler {

    abstract suspend fun resolve(data: String): Either<LatLonExtractError, LatLon>

    abstract fun canResolve(data: String): Boolean

    protected suspend fun HttpClient.getWithSimpleUA(url: String) = get(url) {
        headers {
            append("User-Agent", "Mozilla/5.0")
        }
    }

    protected suspend fun HttpClient.getWithFullUA(url: String) = get(url) {
        headers {
            append(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
            )
        }
    }
}