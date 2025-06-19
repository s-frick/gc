package rocks.frick.gc.controller;

import static java.lang.String.format;

import static scala.jdk.javaapi.OptionConverters.*;
import static scala.jdk.javaapi.CollectionConverters.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gc.model.Activity;
import gc.Pageable;
import gc.application.GetFullActivityService;
import gc.application.ListActivityService;
import gc.application.UpdateActivityNameService;
import gc.application.UploadActivityService;
import gc.model.FileID;

@RestController
@RequestMapping("/api/v1")
public class UploadWorkoutController {

  private static final Logger log = LoggerFactory.getLogger(UploadWorkoutController.class);

  private final UploadActivityService uploadWorkoutService;
  private final ListActivityService listWorkoutsService;
  private final GetFullActivityService getFullWorkoutService;
  private final UpdateActivityNameService updateActivityNameService;

  public UploadWorkoutController(UploadActivityService uploadWorkoutService, ListActivityService listWorkoutsService,
      GetFullActivityService getFullWorkoutService, UpdateActivityNameService updateActivityNameService) {
    this.uploadWorkoutService = uploadWorkoutService;
    this.listWorkoutsService = listWorkoutsService;
    this.getFullWorkoutService = getFullWorkoutService;
    this.updateActivityNameService = updateActivityNameService;
  }

  @PostMapping("/upload-workout")
  public ResponseEntity<String> uploadWorkout(@RequestParam("file") MultipartFile file) {
    InputStream inputStream = null;
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      return ResponseEntity.badRequest().body("Failed to read the uploaded file");
    }

    var filename = file.getOriginalFilename();
    Activity ride = uploadWorkoutService.upload(inputStream, filename);
    return ResponseEntity.ok(format("Workout file uploaded successfully. ID: %s", ride.id()));
  }

  // TODO: extract listWorkouts
  @GetMapping("/workouts/{id}")
  public ResponseEntity<Activity> workoutById(@PathVariable String id) {
    log.debug("Retrieving workout by ID: {}", id);
    var workout = toJava(getFullWorkoutService.getFullWorkout(id));
    return workout
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  record UpdateWorkoutNameRequest(String name) {
  }

  // TODO: extract listWorkouts
  @PostMapping("/workouts/{id}")
  public ResponseEntity<Void> updateWorkoutName(@PathVariable String id, @RequestBody UpdateWorkoutNameRequest req) {
    log.debug("Updating workout name by ID: {}", id);
    updateActivityNameService.updateActivityName(id, req.name());
    return ResponseEntity.noContent().build();
  }

  // TODO: extract listWorkouts
  @GetMapping("/workouts")
  public ResponseEntity<List<FileIdTo>> getWorkouts() {
    var workouts = asJava(listWorkoutsService.listActivities(new Pageable(0, 10)));
    return ResponseEntity.ok(
      workouts.stream()
        .flatMap(w -> FileIdTo.fromDomain(w.fileID(), w.id(), w.name()).stream())
        .toList()
    );
  }

}
