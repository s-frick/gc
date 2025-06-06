package gc.parsers

import gc.model._
import com.garmin.fit._
import java.io.InputStream
import scala.collection.mutable.ListBuffer

class FitRideParser extends RideParser {

  override def parseFromStream(inputStream: InputStream): Ride = {
    val buf = inputStream.readAllBytes()
    val decoder = new Decode()
    if (!decoder.checkFileIntegrity(new ByteArrayDataInputStream(buf))) {
      throw new IllegalArgumentException("FIT file integrity check failed")
    }

    val mesgBroadcaster = new MesgBroadcaster(decoder)
    val listener = new FitListener()

    mesgBroadcaster.addListener(listener.asInstanceOf[RecordMesgListener])
    mesgBroadcaster.addListener(listener.asInstanceOf[MonitoringMesgListener])
    mesgBroadcaster.addListener(listener.asInstanceOf[DeviceInfoMesgListener])
    mesgBroadcaster.addListener(listener.asInstanceOf[UserProfileMesgListener])
    mesgBroadcaster.addListener(listener.asInstanceOf[FileIdMesgListener])

    val ok = decoder.read(new ByteArrayDataInputStream(buf), mesgBroadcaster, mesgBroadcaster)
    if (ok) { printf("Successfully decoded FIT file.\n") } else { printf("Failure decoding FIT file.") }

    Ride(
      samples = listener.samples.toSeq, 
      source = "fit", 
      fileID = listener.fileId, 
      userProfile = listener.userProfile
    )
  }
}

class FitListener extends FileIdMesgListener, UserProfileMesgListener, DeviceInfoMesgListener, MonitoringMesgListener, RecordMesgListener, DeveloperFieldDescriptionListener {
  var fileId:      Option[FileID]         = None
  var userProfile: Option[UserProfile]    = None
  val samples:     ListBuffer[RideSample] = ListBuffer[RideSample]()

  override def onMesg(msg: FileIdMesg): Unit = {
    fileId = Some(FileID(
      f_type         =  msg.getType.name,
      manufacturer   =  msg.getManufacturer.toInt,
      product        =  msg.getProduct.toInt,
      product_name   =  msg.getProductName,
      serial_number  =  msg.getSerialNumber.toLong,
      number         =  msg.getNumber.toInt
    ))
  }

  override def onMesg(msg: UserProfileMesg): Unit = {
    userProfile = Some(UserProfile(
      friendly_name = msg.getFriendlyName,
      gender = msg.getGender.name,
      age = msg.getAge.toInt,
      weight = msg.getWeight.toFloat
    ))
  }

  override def onMesg(msg: RecordMesg): Unit = {
    val timestamp = Option(msg.getTimestamp)
      .map(_.getTimestamp)              // seconds since 1989-12-31
      .map(_ + 631065600L)              // convert to UNIX epoch
      .getOrElse(0L)
    val sample = RideSample(
        timestamp = timestamp,
        power     = Option(msg.getPower).map(_.toInt),
        heartRate = Option(msg.getHeartRate).map(_.toInt),
        cadence   = Option(msg.getCadence).map(_.toInt)
    )

    samples += sample
  }

  override def onDescription(msg: DeveloperFieldDescription): Unit = ()

  override def onMesg(msg: DeviceInfoMesg): Unit = ()

  override def onMesg(msg: MonitoringMesg): Unit = ()

}
