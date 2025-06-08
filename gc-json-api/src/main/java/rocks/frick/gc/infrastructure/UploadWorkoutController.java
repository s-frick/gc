package rocks.frick.gc.infrastructure;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.websocket.server.PathParam;
import rocks.frick.gc.Pageable;
import rocks.frick.gc.Ride;
import rocks.frick.gc.application.GetFullWorkoutService;
import rocks.frick.gc.application.ListWorkoutsService;
import rocks.frick.gc.application.UploadWorkoutService;
import rocks.frick.gc.application.dto.FileIDTo;

@RestController
@RequestMapping("/api/v1")
public class UploadWorkoutController {

  private static final Logger log = LoggerFactory.getLogger(UploadWorkoutController.class);

  private final UploadWorkoutService uploadWorkoutService;
  private final ListWorkoutsService listWorkoutsService;
  private final GetFullWorkoutService getFullWorkoutService;

  public UploadWorkoutController(UploadWorkoutService uploadWorkoutService, ListWorkoutsService listWorkoutsService, GetFullWorkoutService getFullWorkoutService) {
    this.uploadWorkoutService = uploadWorkoutService;
    this.listWorkoutsService = listWorkoutsService;
    this.getFullWorkoutService = getFullWorkoutService;
  }

  @PostMapping("/upload-workout")
  public ResponseEntity<String> uploadWorkout(@RequestParam("file") MultipartFile file) {
    InputStream inputStream = null;
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      return ResponseEntity.badRequest().body("Failed to read the uploaded file");
    }

    String id = uploadWorkoutService.upload(inputStream);
    return ResponseEntity.ok(format("Workout file uploaded successfully. ID: %s", id));
  }

  // TODO: extract listWorkouts
  @GetMapping("/workouts/{id}")
  public ResponseEntity<Ride> workoutById(@PathVariable String id) {
    log.debug("Retrieving workout by ID: {}", id);
    var workout = getFullWorkoutService.getFullWorkout(id);
    return workout
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // TODO: extract listWorkouts
  @GetMapping("/workouts")
  public ResponseEntity<List<FileIDTo>> getWorkouts() {
    var workouts = listWorkoutsService.listWorkouts(Pageable.of(0, 10));
    return ResponseEntity.ok(workouts);
  }

}
