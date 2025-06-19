package gc.spi

import gc.model.Activity

// Java-kompatibel
trait SaveActivity{
  def save(activity: Activity): Activity
}
