package gc.parsers

import gc.model.{Ride, RideSample}
import java.time.Instant

class CsvRideParser extends RideParser {
    override def parse(lines: List[String]): Ride = {
    val samples = lines.drop(1)
      .filterNot(_.isEmpty)
      .map { line =>
        val parts = line.split(",").map(_.trim)
        RideSample(
          timestamp = Instant.parse(parts(0)).getEpochSecond,
          power = parts.lift(5).flatMap(s => if (s.isEmpty) None else Some(s.toInt)),
          heartRate = parts.lift(4).flatMap(s => if (s.isEmpty) None else Some(s.toInt)),
          cadence = parts.lift(6).flatMap(s => if (s.isEmpty) None else Some(s.toInt)),
        )
    }

    Ride(samples, source = "fit")
  }
}

