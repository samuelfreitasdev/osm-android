package br.com.sf.osm_challenge.repository.osm

import br.com.sf.osm_challenge.repository.BoundingBox
import br.com.sf.osm_challenge.repository.Point
import org.junit.Assert
import org.junit.Test
import java.util.regex.Pattern

class OsmRepositoryTest {

    @Test
    fun testGetPOICloseTo() {

        OsmRepository()
            .findPOICloseTo(
                "quixada",
                BoundingBox(
                    Point(-4.9715695, -39.011753),
                    Point(-14.9715695, -19.011753)
                )
            )
            .test()
            .assertNoErrors()
            .awaitCount(5)
            .assertComplete()

    }

    fun hasLatLon(term: String): String {
        val pattern = Pattern.compile("(-?\\d+(\\.\\d+)?),(\\s*(-?\\d+(\\.\\d+)?))")
        val matcher = pattern.matcher(term)

        if (matcher.find()) {
            return matcher.group(0)
        }
        return ""
    }

    @Test
    fun testRegex() {
//        Assert.assertEquals("-4.9715695,-39.011753", hasLatLon("-4.9715695,-39.011753"))
        Assert.assertEquals("-4.9715695,-39.011753", hasLatLon("---4.9715695,-39.011753---"))
    }

}