import { useEffect, useState } from "react";
import "./App.css";
import { ChartAreaGradient } from "./components/ui/chart-area-gradient";
import { WorkoutList } from "./components/ui/workout-list";
import { FileUploader } from "./components/file-uploader";
import { SwitchWithLabel } from "./components/switch-with-label";
import GpsMap from "./components/gps-map";

export type WorkoutSample = {
  speed?: number;
  distance?: number;
  heart_rate?: number;
  power?: number;
  cadence?: number;
  timestamp: number;
  positionLat?: number,
  positionLong?: number
};

function App() {
  const [selectedWorkoutId, setSelectedWorkoutId] = useState<string | null>(
    null,
  );
  const [workout, setWorkout] = useState<WorkoutSample[]>([]);
  const [error, setError] = useState(null);
  const [toggles, setToggles] = useState({
    power: true,
    cadence: true,
    heart_rate: true,
    speed: true
  })
  const setToggle = (key: keyof typeof toggles) => (value: boolean) => {
    setToggles((prev) => ({ ...prev, [key]: value }))
  }

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
    <div className="flex min-h-svh flex-col gap-4 px-2 sm:px-4 lg:px-6 xl:px-8 2xl:px-12">
      <p>{selectedWorkoutId}</p>
      <div className="flex flex-wrap gap-4 justify-center">
        <SwitchWithLabel id="power" checked={toggles.power} label="Leistung" onCheckedChange={setToggle("power")}></SwitchWithLabel>
        <SwitchWithLabel id="cadence" checked={toggles.cadence} label="Trittfrequenz" onCheckedChange={setToggle("cadence")}></SwitchWithLabel>
        <SwitchWithLabel id="heart_rate" checked={toggles.heart_rate} label="Herzfrequenz" onCheckedChange={setToggle("heart_rate")}></SwitchWithLabel>
        <SwitchWithLabel id="speed" checked={toggles.speed} label="Geschwindigkeit" onCheckedChange={setToggle("speed")}></SwitchWithLabel>
      </div>

      {selectedWorkoutId ? (
        <div className="flex flex-col lg:flex-row gap-4 px-4">
          {/* Linke Spalte: Diagramme */}
          <div className="lg:w-2/3 w-full">
            <ChartAreaGradient
              workout={workout}
              workoutId={selectedWorkoutId}
              show={toggles}
            />
          </div>

          {/* Rechte Spalte: Karte */}
          <div className="lg:w-1/3 w-full">
            <GpsMap
              gps={workout
                .filter(w => typeof w.positionLat === "number" && typeof w.positionLong === "number")
                .map(w => [w.positionLat, w.positionLong] as [number, number])}
            />
          </div>
        </div>
      ) : (
        <p>Please select a workout...</p>
      )}
      <FileUploader />

      <WorkoutList
        selected={selectedWorkoutId}
        onSelectWorkout={setSelectedWorkoutId}
      ></WorkoutList>
    </div >
  );
}

export default App;
