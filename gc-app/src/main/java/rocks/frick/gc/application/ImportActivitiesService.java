package rocks.frick.gc.application;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rocks.frick.gc.Ride;
import rocks.frick.gc.application.spi.ActivityProvider;
import rocks.frick.gc.application.spi.ActivityRepository;

public class ImportActivitiesService {

  private static final Logger log = LoggerFactory.getLogger(GetFullActivityService.class);

  private final ActivityRepository activityRepository;

  public ImportActivitiesService(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  public void importActivities(String userId, ActivityProvider provider) {
    // var activities = provider.newActivities(userId, )

  }
}
