package gc.spi

import gc.model.Activity
import java.util.Optional

// Java-kompativel
trait FindActivityById {
  def findById(id: String): Option[Activity]
}
