package rocks.frick.gc;

/**
 * Represents a single data point recorded during a ride.
 * <p>
 * A {@code RideSample} corresponds to one moment in time and contains sensor
 * data such as
 * heart rate, power, and cadence.
 *
 * @param timestamp  The timestamp of the sample in seconds since the epoch
 *                   (UTC).
 * @param heart_rate The heart rate in beats per minute (BPM), or 0 if
 *                   unavailable.
 * @param power      The power output in watts, or 0 if unavailable.
 * @param cadence    The pedaling cadence in revolutions per minute (RPM), or 0
 *                   if unavailable.
 */
public record RideSample(
    long timestamp,
    float speed,
    float distance,
    int heart_rate,
    int power,
    int cadence,
    float positionLat,
    float positionLong) {
}
