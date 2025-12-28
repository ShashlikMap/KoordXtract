package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.Some

/**
 * Abstract data resolver to [LatLon]
 */
abstract class MapDataHandler {
    protected companion object {
        val GENERAL_LAT_LON_REGEX =
            Regex("([-+]?\\d*\\.?\\d+)[, ]\\s*([-+]?\\d*\\.?\\d+)")
    }

    /**
     * Extracts [LatLon] from [data]
     */
    abstract suspend fun resolve(data: String): Either<LatLonExtractError, LatLon>

    /**
     * Quick check if [LatLon] can be extracted from [data]
     */
    abstract fun canResolve(data: String): Boolean

    /**
     * Convenient general [String] extension to extract [LatLon] using [regex].
     * [latLonReversed] is used to swap the Lat and Lon
     */
    protected fun String.tryExtract(
        regex: Regex,
        latLonReversed: Boolean = false,
    ): Option<LatLon> {
        regex.find(this)?.let { match ->
            val val1 = match.groupValues[1].toDoubleOrNull()
            val val2 = match.groupValues[2].toDoubleOrNull()

            if (val1 != null && val2 != null) {
                return if (latLonReversed) {
                    Some(LatLon(val2, val1))
                } else {
                    Some(LatLon(val1, val2))
                }
            }
        }

        return None
    }
}
