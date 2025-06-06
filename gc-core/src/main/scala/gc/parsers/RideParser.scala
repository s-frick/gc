package gc.parsers

import gc.model.{Ride}
import java.io.InputStream

trait RideParser {
  def parse(lines: List[String]): Ride = {
    throw new UnsupportedOperationException("This parser doesn't support stream-based input")
  }


  def parseFromStream(inputStream: InputStream): Ride = {
    throw new UnsupportedOperationException("This parser doesn't support stream-based input")
  }
}


