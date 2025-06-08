package rocks.frick.gc.application.port;

import java.util.List;

import rocks.frick.gc.Pageable;
import rocks.frick.gc.Ride;
import rocks.frick.gc.application.dto.FileIDTo;

public interface WorkoutRepository {

  /**
   * Saves a workout to the repository.
   *
   * @param workout the workout to save
   * @return the ID of the saved workout
   */
  String save(Ride workout);

  /**
   * Finds a workout by its ID.
   *
   * @param id the ID of the workout
   * @return the found workout, or null if not found
   */
  Ride findById(String id);

  /**
   * Deletes a workout by its ID.
   *
   * @param id the ID of the workout to delete
   */
  void deleteById(String id);

  List<FileIDTo> findAll(Pageable page);
}
