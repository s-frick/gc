package gc.spi

// Java-kompatibel
trait UpdateActivityName {
  def updateActivityName(id: String, name: String): Unit
}
