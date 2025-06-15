package rocks.frick.gc.application;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.Ride;
import rocks.frick.gc.WorkoutAnalyzer;
import rocks.frick.gc.WorkoutSummary;
import rocks.frick.gc.application.spi.ActivityRepository;

public class UploadActivityService {

  private static final Logger log = LoggerFactory.getLogger(UploadActivityService.class);

  private final ActivityRepository activityRepository;
  private final WorkoutAnalyzer analyzer;

  public UploadActivityService(ActivityRepository activityRepository, WorkoutAnalyzer analyzer) {
    this.activityRepository = activityRepository;
    this.analyzer = analyzer;
  }

  public String upload(InputStream data, String name) {
    WorkoutSummary summary = analyzer.analyze(data);

    log.debug("{}", summary);
    var ride = new Ride(
      summary.ride().id(),
      summary.ride().samples(),
      name,
      summary.ride().source(),
      summary.ride().file_id(),
      summary.ride().user_profile()
    );

    return activityRepository.save(ride);

  }
}
