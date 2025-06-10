package gc

import scala.jdk.CollectionConverters._

import rocks.frick.gc.{WorkoutAnalyzer, WorkoutSummary}
import java.io.InputStream
import java.util.Optional
import gc.parsers.FitRideParser
import gc.parsers.RideParser
import gc.model.RideSample
import gc.model.FileID

class WorkoutAnalyzerImpl extends WorkoutAnalyzer {
  val parser = new FitRideParser()
  override def analyze(dataStream: InputStream): WorkoutSummary = {
    // Beispielhafte Analyse-Logik
    val ride = parser.parseFromStream(dataStream)

    val j_ride: rocks.frick.gc.Ride = new rocks.frick.gc.Ride(
      samples = ride.samples.map { toJava _ }.asJava,
      source = ride.source,
      file_id = toJava(ride.fileID),
      user_profile = toJava(ride.userProfile)
      )
    new WorkoutSummary("Tolles Workout", j_ride)
  }

  def toJava(sample: RideSample): rocks.frick.gc.RideSample = {
    new rocks.frick.gc.RideSample(
      timestamp     =  sample.timestamp,
      speed         =  sample.speed.getOrElse(0),
      power         =  sample.power.getOrElse(0),
      cadence       =  sample.cadence.getOrElse(0),
      heart_rate    =  sample.heartRate.getOrElse(0),
      distance      =  sample.distance.getOrElse(0.0f),
      positionLat   =  sample.positionLat.getOrElse(0),
      positionLong  =  sample.positionLong.getOrElse(0)
    )
  }

  def toJava(file_id: Option[FileID]): rocks.frick.gc.FileID = {
    val f_type        = file_id map(f => f.f_type)        getOrElse "unknown"
    val manufacturer  = file_id map(f => f.manufacturer)  getOrElse 0
    val product       = file_id map(f => f.product)       getOrElse 0
    val product_name  = file_id map(f => f.product_name)  getOrElse "unknown"
    val serial_number = file_id map(f => f.serial_number) getOrElse 0L
    val number        = file_id map(f => f.number)        getOrElse 0

    new rocks.frick.gc.FileID(
      f_type         =  f_type,
      manufacturer   =  manufacturer,
      product        =  product,
      product_name   =  product_name,
      serial_number  =  serial_number,
      number         =  number
    )
  }

  def toJava(userProfile: Option[gc.model.UserProfile]): rocks.frick.gc.UserProfile = {
    val friendly_name = userProfile map(f => f.friendly_name) getOrElse "unknown"
    val gender        = userProfile map(f => f.gender)        getOrElse "unknown"
    val age           = userProfile map(f => f.age)           getOrElse 0
    val weight        = userProfile map(f => f.weight)        getOrElse 0.0f

    new rocks.frick.gc.UserProfile(
      friendly_name  =  friendly_name,
      gender         =  gender,
      age            =  age,
      weight         =  weight
    )
  }
}
