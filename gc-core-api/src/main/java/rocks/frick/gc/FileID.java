package rocks.frick.gc;

/**
 * Represents metadata about a workout file, typically extracted from the file
 * header.
 * <p>
 * This information identifies the origin and characteristics of the file,
 * such as the type of device, manufacturer, and a unique serial or file number.
 *
 * @param f_type        The type of file (e.g., "activity", "settings",
 *                      "monitoring"). May follow FIT file conventions.
 * @param manufacturer  The numeric manufacturer ID as defined by the FIT SDK
 *                      specification.
 * @param product       The numeric product ID, specific to the manufacturer.
 * @param product_name  A human-readable product name (e.g., "Edge 530"). May be
 *                      null if not available.
 * @param serial_number The device's unique serial number, used to identify the
 *                      recording device.
 * @param number        A unique file number within the device or session
 *                      context.
 */
public record FileID(
  String  f_type,         
  int     manufacturer,   
  int     product,        
  String  product_name,   
  long    serial_number,  
  int     number)         {
}
