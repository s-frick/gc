package gc.parsers

import gc.model.{Activity}
import java.io.InputStream

trait RideParser {
  def parseFromStream(inputStream: InputStream): Activity = {
    throw new UnsupportedOperationException("This parser doesn't support stream-based input")
  }
}


