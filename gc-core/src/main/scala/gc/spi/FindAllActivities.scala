package gc.spi

import gc.Pageable
import gc.model.Activity

// Java-kompatibel
trait FindAllActivities {
  def findAll(pageable: Pageable): List[Activity]
}
