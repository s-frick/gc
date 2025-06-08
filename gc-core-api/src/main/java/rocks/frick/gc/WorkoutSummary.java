package rocks.frick.gc;

/**
 * Represents a high-level summary of a workout after analysis.
 * <p>
 * This summary typically includes metadata extracted from the file, such as the
 * data source,
 * and may be extended with additional metrics like duration, average power,
 * distance, etc.
 *
 * @param source The original data source of the workout (e.g., "Fit").
 */
public record WorkoutSummary(String source, Ride ride) {

}
