package gc.parsers

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import gc.model.{RideSample}

import scala.io.Source

class RideParserSpec extends AnyFlatSpec with Matchers {

  "RideParser" should "correctly parse a sample CSV file" in {
    val source = Source.fromResource("sample_ride.csv")
    val lines = source.getLines().toList
    source.close()

    val parser = new CsvRideParser()
    val ride = parser.parse(lines)
    val samples = ride.samples.toArray

    samples.length shouldBe 7
    samples.head shouldBe RideSample(
      timestamp  =  1719820800,
      power      =  Some(0),
      heartRate  =  Some(90),
      cadence    =  Some(0)
    )
    samples.last shouldBe RideSample(
      timestamp  =  1719820806,
      power      =  Some(230),
      heartRate  =  Some(102),
      cadence    =  Some(88)
    )
    // ride.samples.last shouldBe RidePoint(240.0, Some(310.0), Some(150))
  }
}

