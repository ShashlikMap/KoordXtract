package handlers

import LatLon
import arrow.core.None
import arrow.core.Some
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class GoogleMapsMapDataHandlerTest {

    @Test
    fun `check that extracting returns correct results`() {
        val handler = GoogleMapsMapDataHandler()

        val japan = File("src/commonTest/testdata/gmaps_test_japan.txt")
        assertEquals(
            Some(LatLon(139.7442197, 35.6784667)),
            handler.extractLatLonFromUrlContentInternal(japan.readText())
        )

        val usa = File("src/commonTest/testdata/gmaps_test_usa.txt")
        assertEquals(
            Some(LatLon(-117.64672180000001, 34.28889219999999)),
            handler.extractLatLonFromUrlContentInternal(usa.readText())
        )
    }

    @Test
    fun `check that extracting returns None for non-gmaps content`() {
        val handler = GoogleMapsMapDataHandler()

        val someContent1 = "QWQRWTQUWYQUIWIQOBNDJSND#(E(.1.1.3."
        assertEquals(
            None,
            handler.extractLatLonFromUrlContentInternal(someContent1)
        )

        val someContent2 = "-117.64672180000001, 34.28889219999999"
        assertEquals(
            None,
            handler.extractLatLonFromUrlContentInternal(someContent2)
        )
    }
}