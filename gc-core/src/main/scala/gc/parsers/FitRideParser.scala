package gc.parsers

import com.garmin.fit._
import java.io.InputStream
import scala.collection.mutable.ListBuffer
import gc.model.FileID
import gc.model.UserProfile
import gc.model.ActivitySample

class FitRideParser extends RideParser {

  override def parseFromStream(inputStream: InputStream): gc.model.Activity = {
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

    gc.model.Activity(
      id = null,
      name = null,
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
  val samples:     ListBuffer[ActivitySample] = ListBuffer[ActivitySample]()

  override def onMesg(msg: FileIdMesg): Unit = {
    fileId = Some(FileID(
      f_type         =  msg.getType.name,
      product_name   =  msg.getProductName,
      number         =  msg.getNumber.toInt,
      product        =  msg.getProduct.toInt,
      manufacturer   =  msg.getManufacturer.toInt,
      serial_number  =  msg.getSerialNumber.toLong
    ))
  }

  override def onMesg(msg: UserProfileMesg): Unit = {
    val friendlyName = 
    userProfile = Some(UserProfile(
      friendly_name  =  Option(msg.getFriendlyName).filter(_.nonEmpty),
      gender         =  Option(msg.getGender.name).filter(_.nonEmpty),
      weight         =  Option(msg.getWeight.toFloat).filter(_ != 0),
      age            =  Option(msg.getAge.toInt).filter(_ != 0)
    ))
  }

  override def onMesg(msg: RecordMesg): Unit = {
    val timestamp = Option(msg.getTimestamp)
      .map(_.getTimestamp)              // seconds since 1989-12-31
      .map(_ + 631065600L)              // convert to UNIX epoch
      .getOrElse(0L)
      
    val sample = ActivitySample(
      timestamp     =  timestamp,
      power         =  Option(msg.getPower).filter(_ != 0).map(_.toInt),
      cadence       =  Option(msg.getCadence).filter(_ != 0).map(_.toInt),
      speed         =  Option(msg.getSpeed).filter(_ != 0).map(_.toFloat),
      heartRate     =  Option(msg.getHeartRate).filter(_ != 0).map(_.toInt),
      distance      =  Option(msg.getDistance).filter(_ != 0).map(_.toFloat),
      positionLat   =  Option(msg.getPositionLat).filter(_ != 0).map(toDecimalDegrees _),
      positionLong  =  Option(msg.getPositionLong).filter(_ != 0).map(toDecimalDegrees _)
    )

    samples += sample
  }

  def toDecimalDegrees(x: Integer): Float = {
    (x * (180 / Math.pow(2, 31))).toFloat
  }

  override def onDescription(msg: DeveloperFieldDescription): Unit = ()

  override def onMesg(msg: DeviceInfoMesg): Unit = ()

  override def onMesg(msg: MonitoringMesg): Unit = ()

}
