package rocks.frick.gc;

import java.util.List;

/**
 * Represents a complete workout ride that was recorded by a device. *
 * <p>
 * A {@code Ride} typically consists of timestamped samples (e.g., power, heart
 * rate, cadence),
 * metadata about the source of the file, and contextual user and device
 * information.
 *
 * @param samples      The list of recorded samples for this ride (e.g., one per
 *                     second). May be empty but never null.
 * @param source       The technical source of the file (e.g., "Fit").
 * @param file_id      Metadata about the file itself, such as creation time,
 *                     manufacturer, and product type.
 * @param user_profile The user profile information at the time of recording
 *                     (e.g., weight, FTP, max HR).
 */
public record Ride(
  List<RideSample>  samples,       
  String            source,        
  FileID            file_id,       
  UserProfile       user_profile)  
{}
