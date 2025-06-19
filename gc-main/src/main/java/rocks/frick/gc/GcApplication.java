
package rocks.frick.gc;

import static scala.jdk.javaapi.OptionConverters.*;
import static scala.jdk.javaapi.CollectionConverters.*;
import scala.collection.immutable.List;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;

import gc.WorkoutAnalyzer;
import gc.WorkoutAnalyzerImpl;
import gc.application.GetFullActivityService;
import gc.application.ListActivityService;
import gc.application.UpdateActivityNameService;
import gc.application.UploadActivityService;
import gc.spi.FindActivityById;
import gc.spi.FindAllActivities;
import gc.spi.SaveActivity;
import gc.spi.UpdateActivityName;

/**
 * Main entry point for the GoldenCheetah Core Spring Boot application.
 */
@SpringBootApplication
public class GcApplication {

  private static final Map<String, gc.model.Activity> rides = new HashMap<>();

  private static final Logger log = LoggerFactory.getLogger(GcApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(GcApplication.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return Jackson2ObjectMapperBuilder.json()
        .modules(new DefaultScalaModule())
        .build();
  }

  @Bean
  public static UploadActivityService uploadWorkoutService(
      SaveActivity repo, WorkoutAnalyzer analyzer) {
    return new UploadActivityService(repo, analyzer);
  }

  @Bean
  public static ListActivityService listWorkoutsService(
      FindAllActivities repo) {
    return new ListActivityService(repo);
  }

  @Bean
  public static GetFullActivityService getFullWorkoutService(
      FindActivityById repo) {
    return new GetFullActivityService(repo);
  }

  @Bean
  public static WorkoutAnalyzer workoutAnalyzer() {
    return new WorkoutAnalyzerImpl();
  }

  @Bean
  public static UpdateActivityNameService updateActivityNameService(UpdateActivityName repo) {
    return new UpdateActivityNameService(repo);
  }

  // Repository Stubs 
  @Bean
  public static SaveActivity saveActivity() {
    return workout -> {
      var w = workout.id() == null
          ? new gc.model.Activity(
              UUID.randomUUID().toString(),
              workout.name(),
              workout.samples(),
              workout.source(),
              workout.fileID(),
              workout.userProfile())
          : workout;

      rides.put(w.id(), w);
      // log.info("Saved workouts: {}",
      // rides.values().stream().map(Ride::name).toList());
      return w;
    };
  }

  @Bean
  public static FindActivityById findActivityById() {
    return id -> {
      var ride = rides.get(id);
      return toScala(Optional.ofNullable(ride));
    };
  }

  @Bean
  public static FindAllActivities findAllActivities() {
    return page -> List.from(asScala(rides.entrySet().stream()
        .skip(page.page() * page.size())
        .limit(page.size())
        .map(e -> e.getValue())
        .toList()));
  }

  @Bean
  public static UpdateActivityName updateActivityName(FindActivityById findActivityById, SaveActivity saveActivity) {
    return (String id, String name) -> {
      var activity = findActivityById.findById(id);
      log.debug("Updating name of activity with ID {} to '{}'", id, name);
      toJava(activity).ifPresent(a -> saveActivity.save(new gc.model.Activity(
          a.id(),
          name,
          a.samples(),
          a.source(),
          a.fileID(),
          a.userProfile())));
    };
  }

}
