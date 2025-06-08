package rocks.frick.gc;

/**
 * Represents a snapshot of user-specific profile information at the time of a
 * recorded ride.
 * <p>
 * This metadata can be used for personalized analysis, such as power-to-weight
 * ratio or age-based zone calculations.
 *
 * @param friendly_name A display name or nickname for the user (e.g.
 *                      "Sebastian").
 * @param gender        The user's gender as a string (e.g. "male", "female",
 *                      "other"); may be null or empty if unspecified.
 * @param age           The user's age in full years at the time of the
 *                      activity.
 * @param weight        The user's weight in kilograms (kg), used for
 *                      power-to-weight and calorie calculations.
 */
public record UserProfile(
  String  friendly_name,  
  String  gender,         
  int     age,            
  float   weight)         {
}
