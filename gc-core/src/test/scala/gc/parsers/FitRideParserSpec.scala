package gc.parsers

import org.scalatest.funsuite.AnyFunSuite
import gc.model.Activity
import java.io.FileInputStream
import scala.io.Source

class FitRideParserSpec extends AnyFunSuite {

  test("FitRideParser should parse at least one sample from FIT file") {
    val parser = new FitRideParser()
    val resourceStream = getClass.getResourceAsStream("/test.fit")
    // val inputStream = new FileInputStream("test-data/test.fit") // Pfad anpassen

    val ride: Activity = parser.parseFromStream(resourceStream)
    resourceStream.close()

// RideSample(1725781019,Some(167),None,Some(0))
// RideSample(1725796482,Some(168),None,Some(0))


    assert(ride.samples.nonEmpty, "Ride should contain at least one sample")
  }
}

