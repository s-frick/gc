
package rocks.frick.gc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import gc.WorkoutAnalyzerImpl;
import rocks.frick.gc.application.GetFullActivityService;
import rocks.frick.gc.application.ListActivityService;
import rocks.frick.gc.application.UpdateActivityNameService;
import rocks.frick.gc.application.UploadActivityService;
import rocks.frick.gc.application.dto.FileIDTo;
import rocks.frick.gc.application.spi.ActivityRepository;

/**
 * Main entry point for the GoldenCheetah Core Spring Boot application.
 */
@SpringBootApplication
public class GcApplication {

  private static final Logger log = LoggerFactory.getLogger(GcApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(GcApplication.class, args);
  }

  @Bean
  public static UploadActivityService uploadWorkoutService(
      ActivityRepository workoutRepository, WorkoutAnalyzer analyzer) {
    return new UploadActivityService(workoutRepository, analyzer);
  }

  @Bean
  public static ListActivityService listWorkoutsService(
      ActivityRepository workoutRepository) {
    return new ListActivityService(workoutRepository);
  }

  @Bean
  public static GetFullActivityService getFullWorkoutService(
      ActivityRepository workoutRepository) {
    return new GetFullActivityService(workoutRepository);
  }

  @Bean
  public static WorkoutAnalyzer workoutAnalyzer() {
    return new WorkoutAnalyzerImpl();
  }

  @Bean
  public static UpdateActivityNameService updateActivityNameService(ActivityRepository activityRepository) {
    return new UpdateActivityNameService(activityRepository);
  }

  @Bean
  public static ActivityRepository workoutRepository() {
    return new ActivityRepository() {
      private final Map<String, Ride> rides = new HashMap<>();

      @Override
      public String save(Ride workout) {
        var w = workout.id() == null 
          ? new Ride(
            UUID.randomUUID().toString(),
            workout.samples(),       
            workout.name(),
            workout.source(),        
            workout.file_id(),
            workout.user_profile()
          ) : workout;

        rides.put(w.id(), w);
        log.info("Saved workouts: {}", rides.values().stream().map(Ride::name).toList());
        return w.id();
      }

      @Override
      public Ride findById(String id) {
        var ride = rides.get(id);
        if (ride == null) {
          log.warn("Workout with ID {} not found", id);
          return null;
        }
        log.debug("Finding workout by ID: {} - {}", id, ride.file_id());
        return ride;
      }

      @Override
      public void deleteById(String id) {
        rides.remove(id);
      }

      @Override
      public List<FileIDTo> findAll(Pageable page) {
        return rides.entrySet().stream()
            .skip(page.page() * page.size())
            .limit(page.size())
            .map(e -> new FileIDTo(e.getKey(), e.getValue().name(), e.getValue().file_id()))
            .toList();
      }

      @Override
      public void updateActivityName(String id, String newName) {
        var activity = findById(id);
        log.debug("Updating name of activity with ID {} to '{}'", id, newName);
        save(new Ride(
            activity.id(),
            activity.samples(),
            newName,
            activity.source(),
            activity.file_id(),
            activity.user_profile()
        ));
      }

    };
  }

}
