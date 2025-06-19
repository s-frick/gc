package gc.application

import scala.jdk.OptionConverters._

import com.typesafe.scalalogging.Logger
import gc.spi.FindActivityById
import gc.model.Activity

class  GetFullActivityService(activityRepository: FindActivityById) {
  private val log = Logger("GetFullActivityService")

  def getFullWorkout(id: String): Option[Activity] = {
    log.debug(s"[CORE] Retrieving full workout for ID: $id")
    activityRepository.findById(id)
  }
}
