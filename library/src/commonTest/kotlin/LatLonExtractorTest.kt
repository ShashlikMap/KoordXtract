import arrow.core.left
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LatLonExtractorTest {

    @Test
    fun `check different url returns error if data is null`() = runTest {
        val latLonExtractor = LatLonExtractor()

        assertEquals(
            expected = LatLonExtractError.NoData.left(),
            actual = latLonExtractor.extractFromStringData(null)
        )
    }

    @Test
    fun `check different url returns correct results`() = runTest {
        val latLonExtractor = LatLonExtractor()

        // japan
        assertEquals(
            expected = LatLon(35.791951600000004, 139.6641148),
            actual = latLonExtractor.extractFromStringData("https://maps.app.goo.gl/HSUQp6K9L8z5efAY7")
                .getOrNull()
        )

        // usa
        assertEquals(
            expected = LatLon(33.8114365, -118.13840819999999),
            actual = latLonExtractor.extractFromStringData("https://maps.app.goo.gl/nWx4HL35gqPhHrif8")
                .getOrNull()
        )

        // OSMAnd map
        assertEquals(
            expected = LatLon(35.72795, 139.78278),
            actual = latLonExtractor.extractFromStringData(
                "亀\n" +
                        "        日暮里中央通り (東日暮里五丁目), 荒川区\n" +
                        "        Location: geo:35.72795,139.78278?z=17\n" +
                        "        https://osmand.net/map?pin=35.72795,139.78278#17/35.72795/139.78278"
            )
                .getOrNull()
        )

        // from Apple Maps
        assertEquals(
            expected = LatLon(37.789303, -122.409737),
            actual = latLonExtractor.extractFromStringData("https://maps.apple/p/ogrw-5mF-Pjyax")
                .getOrNull()
        )

        // from iOS Google Maps
        assertEquals(
            expected = LatLon(35.7223556, 139.7793444),
            actual = latLonExtractor.extractFromStringData("https://maps.app.goo.gl/PauhWtcmNPCUopxW9")
                .getOrNull()
        )
    }
}