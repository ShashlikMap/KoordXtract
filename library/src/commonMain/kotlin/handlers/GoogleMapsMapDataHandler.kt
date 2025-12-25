package handlers

import io.ktor.client.HttpClient

class GoogleMapsMapDataHandler(httpClient: HttpClient) : MapDataHandler(httpClient) {
    override fun canResolve(data: String): Boolean {
        return data.startsWith("https://maps.app.goo.gl/")
    }
}