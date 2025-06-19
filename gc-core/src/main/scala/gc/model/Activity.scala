package gc.model

import scala.compiletime.ops.float

case class ActivitySample(
  timestamp:     Long,
  speed:         Option[Float],
  heartRate:     Option[Int],
  power:         Option[Int],
  cadence:       Option[Int],
  distance:      Option[Float],
  positionLat:   Option[Float],
  positionLong:  Option[Float]
)

case class FileID(
  f_type:        String,
  manufacturer:  Int,
  product:       Int,
  product_name:  String,
  serial_number: Long,
  number:        Int
)

case class UserProfile(
  friendly_name:  Option[String],
  gender:         Option[String],
  age:            Option[Int],
  weight:         Option[Float]
)

case class Activity(
  id:           String,
  name:         String,
  samples:      Seq[ActivitySample],
  source:       String,
  fileID:       Option[FileID],
  userProfile:  Option[UserProfile]
)

case class WorkoutSummary(source: String, activity: Activity)
