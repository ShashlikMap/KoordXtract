package handlers

import LatLon

class GoogleMapsMapDataHandler : MapDataHandler {
    override suspend fun resolve(data: String): LatLon? {
        if (!data.startsWith("https://maps.app.goo.gl/")) return null
        return LatLon(1.0, 10.0)
    }
}