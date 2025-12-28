package handlers

import LatLon
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GoogleMapsMapDataHandlerTest {

    @Test
    fun `check that checkResolve return correct value`() = runTest {
        val handler = GoogleMapsMapDataHandler()
        assertTrue { handler.canResolve("https://maps.app.goo.gl/HSUQp6K9L8z5efAY7") }
        assertFalse { handler.canResolve("https://www.google.gl/HSUQp6K9L8z5efAY7") }
    }

    @Test
    fun `check google maps url can be resolved correctly`() = runTest {
        val handler = GoogleMapsMapDataHandler()
        // japan
        assertEquals(
            LatLon(35.791951600000004, 139.6641148),
            handler.resolve("https://maps.app.goo.gl/HSUQp6K9L8z5efAY7").getOrNull()
        )

        // usa
        assertEquals(
            LatLon(33.8114365, -118.13840819999999),
            handler.resolve("https://maps.app.goo.gl/nWx4HL35gqPhHrif8").getOrNull()
        )

        // from iOS Google Maps
        assertEquals(
            LatLon(35.7223556, 139.7793444),
            handler.resolve("https://maps.app.goo.gl/PauhWtcmNPCUopxW9").getOrNull()
        )
    }

    @Test
    fun `check that extracting returns correct results`() = runTest {
        mockEngineTest(
            File("src/androidHostTest/testdata/gmaps_test_japan.txt"),
            LatLon(35.6784667, 139.7442197)
        )
        mockEngineTest(
            File("src/androidHostTest/testdata/gmaps_test_usa.txt"),
            LatLon(34.28889219999999, -117.64672180000001)
        )
        mockEngineTest(
            File("src/androidHostTest/testdata/gmaps_test_broken.txt"),
            null
        )
    }

    private suspend fun mockEngineTest(file: File, result: LatLon?) {
        val mockEngine = MockEngine { _ ->
            respond(
                content = file.readText(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/html; charset=utf-8")
            )
        }
        val handler = GoogleMapsMapDataHandler(httpClientLogs = false, HttpClient(mockEngine))
        assertEquals(
            result,
            handler.resolve("anyurl").getOrNull()
        )
    }
}