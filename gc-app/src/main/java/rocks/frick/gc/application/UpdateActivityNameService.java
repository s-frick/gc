package rocks.frick.gc.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.application.spi.ActivityRepository;

public class UpdateActivityNameService {

  private static final Logger log = LoggerFactory.getLogger(UpdateActivityNameService.class);

  private final ActivityRepository activityRepository;

  public UpdateActivityNameService(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  public void updateActivityName(String activityId, String newName) {
    log.debug("Updating name of activity with ID {} to '{}'", activityId, newName);
    activityRepository.updateActivityName(activityId, newName);
  }
}
