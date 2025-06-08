package rocks.frick.gc.application;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.Ride;
import rocks.frick.gc.application.port.WorkoutRepository;

public class GetFullWorkoutService {

  private static final Logger log = LoggerFactory.getLogger(GetFullWorkoutService.class);

  private final WorkoutRepository workoutRepository;

  public GetFullWorkoutService(WorkoutRepository workoutRepository) {
    this.workoutRepository = workoutRepository;
  }

  public Optional<Ride> getFullWorkout(String id) {
    log.debug("Retrieving full workout for ID: {}", id);
    Ride workout = workoutRepository.findById(id);
    
    return Optional.ofNullable(workout);
  }
}
