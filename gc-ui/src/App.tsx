import { useEffect, useState } from "react";
import "./App.css";
import { Button } from "./components/ui/button";
import { ChartAreaGradient } from "./components/ui/chart-area-gradient";
import { WorkoutList } from "./components/ui/workout-list";
import { FileUploader } from "./components/file-uploader";

export type WorkoutSample = {
  heart_rate: number;
  power: number;
  cadence: number;
  timestamp: number;
};

function App() {
  const [selectedWorkoutId, setSelectedWorkoutId] = useState<string | null>(
    null,
  );
  const [workout, setWorkout] = useState<WorkoutSample[]>([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (selectedWorkoutId)
      fetch(`/api/v1/workouts/${selectedWorkoutId}`)
        .then((res) => {
          if (!res.ok) throw new Error("Fehler beim Laden");
          return res.json();
        })
        .then((data: { samples: WorkoutSample[] }) => {
          console.log("First data point", data.samples[0]);
          setWorkout(data.samples);
        })
        .catch((err) => {
          setError(err.message);
        });
  }, [selectedWorkoutId]);

  return (
    <div className="flex min-h-svh flex-col gap-4">
      <p>{selectedWorkoutId}</p>
      {selectedWorkoutId ? (
        <ChartAreaGradient
          workout={workout}
          workoutId={selectedWorkoutId}
        ></ChartAreaGradient>
      ) : (
        <p>Please select a workout to view the chart.</p>
      )}
      <FileUploader />

      <WorkoutList
        selected={selectedWorkoutId}
        onSelectWorkout={setSelectedWorkoutId}
      ></WorkoutList>
    </div>
  );
}

export default App;
