
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
import rocks.frick.gc.application.GetFullWorkoutService;
import rocks.frick.gc.application.ListWorkoutsService;
import rocks.frick.gc.application.UploadWorkoutService;
import rocks.frick.gc.application.dto.FileIDTo;
import rocks.frick.gc.application.port.WorkoutRepository;

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
  public static UploadWorkoutService uploadWorkoutService(
      WorkoutRepository workoutRepository, WorkoutAnalyzer analyzer) {
    return new UploadWorkoutService(workoutRepository, analyzer);
  }

  @Bean
  public static ListWorkoutsService listWorkoutsService(
      WorkoutRepository workoutRepository) {
    return new ListWorkoutsService(workoutRepository);
  }

  @Bean
  public static GetFullWorkoutService getFullWorkoutService(
      WorkoutRepository workoutRepository) {
    return new GetFullWorkoutService(workoutRepository);
  }

  @Bean
  public static WorkoutAnalyzer workoutAnalyzer() {
    return new WorkoutAnalyzerImpl();
  }

  @Bean
  public static WorkoutRepository workoutRepository() {
    return new WorkoutRepository() {
      private final Map<String, Ride> rides = new HashMap<>();

      @Override
      public String save(Ride workout) {
        var id = UUID.randomUUID().toString();
        rides.put(id, workout);
        log.info("Saved workouts: {}", rides.values().stream().map(Ride::file_id).toList());
        return id;
      }

      @Override
      public Ride findById(String id) {
        var ride = rides.get(id);
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
            .map(e -> new FileIDTo(e.getKey(), e.getValue().file_id()))
            .toList();
      }

    };
  }

}
