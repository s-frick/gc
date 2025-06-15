package rocks.frick.gc.application.spi;

import java.time.LocalDate;
import java.util.List;

import rocks.frick.gc.WorkoutSummary;

public interface ActivityProvider {
  List<WorkoutSummary> newActivities(String userId, String type, LocalDate startDate, LocalDate endDate);
}
