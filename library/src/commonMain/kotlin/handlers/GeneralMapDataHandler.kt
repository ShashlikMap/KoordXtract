package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either
import arrow.core.left

class GeneralMapDataHandler : MapDataHandler() {

    override fun canResolve(data: String): Boolean {
        return true
    }

    override suspend fun resolve(data: String): Either<LatLonExtractError, LatLon> {
        return LatLonExtractError.GeneralError("Not implemented yet").left()
    }
}