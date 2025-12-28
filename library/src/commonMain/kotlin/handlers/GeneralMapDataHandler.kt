package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either

class GeneralMapDataHandler : MapDataHandler() {

    override fun canResolve(data: String): Boolean {
        // we can always resolve general text
        return true
    }

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        return either {
            data.tryExtract(GENERAL_LAT_LON_REGEX)
                .getOrElse { raise(LatLonExtractError.Failed) }
        }
    }
}