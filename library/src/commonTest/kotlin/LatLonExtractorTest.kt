import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LatLonExtractorTest {
    @Test
    fun `check different url returns correct results`() = runTest {
        val latLonExtractor = LatLonExtractor()

        // japan
        assertEquals(
            LatLon(35.791951600000004, 139.6641148),
            latLonExtractor.extractFromStringData("https://maps.app.goo.gl/HSUQp6K9L8z5efAY7")
                .getOrNull()
        )

        // usa
        assertEquals(
            LatLon(33.8114365, -118.13840819999999),
            latLonExtractor.extractFromStringData("https://maps.app.goo.gl/nWx4HL35gqPhHrif8")
                .getOrNull()
        )

        // from Apple Maps
        assertEquals(
            LatLon(37.789303, -122.409737),
            latLonExtractor.extractFromStringData("https://maps.apple/p/ogrw-5mF-Pjyax")
                .getOrNull()
        )

        // from iOS Google Maps
        assertEquals(
            LatLon(35.7223556, 139.7793444),
            latLonExtractor.extractFromStringData("https://maps.app.goo.gl/PauhWtcmNPCUopxW9")
                .getOrNull()
        )
    }
}