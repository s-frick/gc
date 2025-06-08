package rocks.frick.gc.application;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.Pageable;
import rocks.frick.gc.application.dto.FileIDTo;
import rocks.frick.gc.application.port.WorkoutRepository;

public class ListWorkoutsService {

  private static final Logger log = LoggerFactory.getLogger(ListWorkoutsService.class);

  private final WorkoutRepository workoutRepository;

  public ListWorkoutsService(WorkoutRepository workoutRepository) {
    this.workoutRepository = workoutRepository;
  }

  public List<FileIDTo> listWorkouts(Pageable page) {
    log.debug("Listing all workouts");
    return workoutRepository.findAll(page); 
  }
}
