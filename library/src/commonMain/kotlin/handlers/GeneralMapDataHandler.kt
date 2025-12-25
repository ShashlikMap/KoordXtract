package handlers

import LatLon

class GeneralMapDataHandler : MapDataHandler {
    override suspend fun resolve(data: String): LatLon? {
        return null
    }
}