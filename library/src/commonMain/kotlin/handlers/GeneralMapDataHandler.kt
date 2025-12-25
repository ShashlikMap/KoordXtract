package handlers

import io.ktor.client.HttpClient

class GeneralMapDataHandler(httpClient: HttpClient) : MapDataHandler(httpClient) {

    override fun canResolve(data: String): Boolean {
        return false
    }
}