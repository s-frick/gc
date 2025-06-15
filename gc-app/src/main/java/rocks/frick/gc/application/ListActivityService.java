package rocks.frick.gc.application;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.Pageable;
import rocks.frick.gc.application.dto.FileIDTo;
import rocks.frick.gc.application.spi.ActivityRepository;

public class ListActivityService {

  private static final Logger log = LoggerFactory.getLogger(ListActivityService.class);

  private final ActivityRepository activityRepository;

  public ListActivityService(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  public List<FileIDTo> listWorkouts(Pageable page) {
    log.debug("Listing all workouts");
    return activityRepository.findAll(page);
  }
}
