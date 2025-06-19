package gc.application

import gc.spi.FindAllActivities
import com.typesafe.scalalogging.Logger
import gc.Pageable
import gc.model.Activity

class ListActivityService(repo: FindAllActivities) {
  private val log = Logger("FindAllActivities")

  def listActivities(page: Pageable): List[Activity] = {
    log.debug(s"[CORE] Listing activities with page: $page.page, size: $page.size")
    repo.findAll(page)
  }

}
