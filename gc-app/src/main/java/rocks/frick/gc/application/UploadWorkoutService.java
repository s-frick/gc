package rocks.frick.gc.application;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.WorkoutAnalyzer;
import rocks.frick.gc.WorkoutSummary;
import rocks.frick.gc.application.port.WorkoutRepository;

public class UploadWorkoutService {

  private static final Logger log = LoggerFactory.getLogger(UploadWorkoutService.class);

  private final WorkoutRepository workoutRepository;
  private final WorkoutAnalyzer analyzer;

  public UploadWorkoutService(WorkoutRepository workoutRepository, WorkoutAnalyzer analyzer) {
    this.workoutRepository = workoutRepository;
    this.analyzer = analyzer;
  }

  public String upload(InputStream data) {
    WorkoutSummary summary = analyzer.analyze(data);

    log.debug("{}", summary);

    return workoutRepository.save(summary.ride());

  }
}
