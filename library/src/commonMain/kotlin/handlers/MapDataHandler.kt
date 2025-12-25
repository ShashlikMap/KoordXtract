package handlers

import LatLon

interface MapDataHandler {
    suspend fun resolve(data: String): LatLon?
}