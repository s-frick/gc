package gc.application

import gc.spi.UpdateActivityName
import com.typesafe.scalalogging.Logger

class UpdateActivityNameService(repo: UpdateActivityName) {
  private val log = Logger("UpdateActivityNameService")

  def updateActivityName(id: String, name: String): Unit = {
    log.debug(s"[CORE] Updating activity name for ID: $id to '$name'")
    repo.updateActivityName(id, name)
  }
}
