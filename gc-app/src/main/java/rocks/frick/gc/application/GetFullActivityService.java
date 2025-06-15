package rocks.frick.gc.application;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.Ride;
import rocks.frick.gc.application.spi.ActivityRepository;

public class GetFullActivityService {

  private static final Logger log = LoggerFactory.getLogger(GetFullActivityService.class);

  private final ActivityRepository activityRepository;

  public GetFullActivityService(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  public Optional<Ride> getFullWorkout(String id) {
    log.debug("Retrieving full workout for ID: {}", id);
    Ride workout = activityRepository.findById(id);

    return Optional.ofNullable(workout);
  }
}
