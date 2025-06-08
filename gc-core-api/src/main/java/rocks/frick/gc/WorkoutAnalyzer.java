package rocks.frick.gc;

import java.io.InputStream;

/**
 * Defines the contract for analyzing workout files and producing structured
 * summaries.
 * <p>
 * Implementations of this interface are responsible for parsing input data
 * (e.g., FIT, TCX, or GPX),
 * extracting relevant metrics, and returning an analyzed summary of the ride.
 */
public interface WorkoutAnalyzer {

  /**
   * Analyzes the given workout input stream and returns a summary.
   * <p>
   * The input stream is expected to contain raw workout data in a supported file
   * format
   * (e.g., .fit, .tcx, .gpx). Implementations should handle format detection and
   * parsing.
   * <p>
   * The stream will be consumed and should not be reused by the caller.
   *
   * @param data an {@link InputStream} of the uploaded workout file; must not be
   *             null
   * @return a {@link WorkoutSummary} containing key performance metrics and
   *         metadata
   */
  WorkoutSummary analyze(InputStream data);

}
