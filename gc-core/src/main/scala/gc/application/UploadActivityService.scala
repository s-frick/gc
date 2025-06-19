package gc.application
import scala.jdk.CollectionConverters._

import gc.WorkoutAnalyzer
import gc.spi.SaveActivity
import com.typesafe.scalalogging.Logger
import java.io.InputStream
import gc.model.Activity

class UploadActivityService(repo: SaveActivity, analyzer: WorkoutAnalyzer) {
  private val log = Logger("UploadActivityService")

  def upload(data: InputStream, name: String): Activity = {
    val summary = analyzer.analyze(data)

      // TODO: kill the Java types
    val ride = Activity(
      id           =  summary.activity.id,
      name         =  name,
      samples      =  summary.activity.samples,
      source       =  summary.source,
      fileID       =  summary.activity.fileID,
      userProfile  =  summary.activity.userProfile
    )
    repo.save(ride)
  }

}
