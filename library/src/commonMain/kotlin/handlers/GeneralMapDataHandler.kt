package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import handlers.MapDataHandler.Companion.GENERAL_LAT_LON_REGEX

/**
 * Resolves any text-based data using common [GENERAL_LAT_LON_REGEX]
 */
class GeneralMapDataHandler : MapDataHandler() {
    override fun canResolve(data: String): Boolean {
        // we can always resolve general text
        return true
    }

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> =
        either {
            data
                .tryExtract(GENERAL_LAT_LON_REGEX)
                .getOrElse { raise(LatLonExtractError.Failed) }
        }
}
