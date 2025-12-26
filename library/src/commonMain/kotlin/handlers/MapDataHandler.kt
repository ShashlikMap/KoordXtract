package handlers

import LatLon
import LatLonExtractError
import arrow.core.Either

abstract class MapDataHandler {
    abstract suspend fun resolve(data: String): Either<LatLonExtractError, LatLon>

    abstract fun canResolve(data: String): Boolean
}