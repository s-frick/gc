package gc


import java.io.InputStream
import java.util.Optional
import gc.parsers.FitRideParser
import gc.parsers.RideParser
import gc.model.Activity
import gc.model.FileID
import gc.model.Activity
import gc.model.UserProfile
import gc.model.WorkoutSummary
import com.typesafe.scalalogging.Logger


trait WorkoutAnalyzer {
  def analyze(data: InputStream): WorkoutSummary;
}

class WorkoutAnalyzerImpl extends WorkoutAnalyzer {
  private val log = Logger("WorkoutAnalyzerImpl")
  val parser = new FitRideParser()
  override def analyze(dataStream: InputStream): WorkoutSummary = {
    // Beispielhafte Analyse-Logik
    val ride = parser.parseFromStream(dataStream)

    log.info(s"CORE: Ride parsed, Samples: ${ride.samples.size}")
    val j_ride: Activity = Activity(
      id = null,
      name = null,
      samples = ride.samples,
      source = ride.source,
      fileID = ride.fileID,
      userProfile = ride.userProfile
      )
    new WorkoutSummary("Tolles Workout", j_ride)
  }
}
