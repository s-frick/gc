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
  heartRate?: number;
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
  const [hoveredGps, setHoveredGps] = useState<[number, number] | null>(null);
  const [error, setError] = useState(null);
  const [toggles, setToggles] = useState({
    power: true,
    cadence: true,
    heartRate: true,
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
          if (!data?.samples || !data?.samples.length || data?.samples?.length === 0) throw new Error("Fehler beim Laden");
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
        <SwitchWithLabel id="power" className="data-[state=checked]:bg-[var(--chart-3)]" checked={toggles.power} label="Leistung" onCheckedChange={setToggle("power")}></SwitchWithLabel>
        <SwitchWithLabel id="cadence" className="data-[state=checked]:bg-[var(--chart-5)]" checked={toggles.cadence} label="Trittfrequenz" onCheckedChange={setToggle("cadence")}></SwitchWithLabel>
        <SwitchWithLabel id="heartRate" className="data-[state=checked]:bg-[var(--chart-1)]" checked={toggles.heartRate} label="Herzfrequenz" onCheckedChange={setToggle("heartRate")}></SwitchWithLabel>
        <SwitchWithLabel id="speed" className="data-[state=checked]:bg-[var(--chart-2)]" checked={toggles.speed} label="Geschwindigkeit" onCheckedChange={setToggle("speed")}></SwitchWithLabel>
      </div>

      {selectedWorkoutId ? (
        <div className="flex flex-col lg:flex-row gap-4 px-4">
          {/* Linke Spalte: Diagramme */}
          <div className="lg:w-2/3 w-full">
            <ChartAreaGradient
              workout={workout}
              workoutId={selectedWorkoutId}
              show={toggles}
              onHoverGps={setHoveredGps}
            />
          </div>

          {/* Rechte Spalte: Karte */}
          <div className="lg:w-1/3 w-full">
            {workout.length > 0 ? (
              <GpsMap
                gps={workout
                  .filter(w => typeof w.positionLat === "number" && typeof w.positionLong === "number")
                  .map(w => [w.positionLat, w.positionLong] as [number, number])}
                currentPos={hoveredGps}
              />
            ) : <p>Keine GPS-Daten verfügbar</p>}
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
