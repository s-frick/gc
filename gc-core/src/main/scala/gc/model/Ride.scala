package gc.model

import scala.compiletime.ops.float

case class RideSample(
  timestamp: Long,
  speed: Option[Float],
  heartRate: Option[Int],
  power:     Option[Int],
  cadence:   Option[Int],
  distance: Option[Float],
  positionLat: Option[Int],
  positionLong: Option[Int],
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
  friendly_name: String,
  gender:        String,
  age:           Int,
  weight:        Float
)

case class Ride(
  samples:     Seq[RideSample],
  source:      String,
  fileID:      Option[FileID] = None,
  userProfile: Option[UserProfile] = None
)
