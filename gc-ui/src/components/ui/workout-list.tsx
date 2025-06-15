import { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"

type Workout = {
  id: string;
  name: string;
}
type WorkoutListProps = {
  selected: string | null;
  onSelectWorkout: (id: string) => void;
}
export function WorkoutList({ selected, onSelectWorkout }: WorkoutListProps) {
  const [workouts, setWorkouts] = useState<Workout[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingWorkout, setEditingWorkout] = useState<Workout | null>(null);

  const getIsSelected = (id: string) => id === selected

  useEffect(() => {
    fetch("/api/v1/workouts")
      .then((res) => {
        if (!res.ok) throw new Error("Fehler beim Laden");
        return res.json();
      })
      .then((data) => {
        setWorkouts(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []); // leeres Array => nur beim ersten Render


  const updateWorkoutName = async (w: Workout) => {
    try {
      const res = await fetch(`/api/v1/workouts/${w.id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name: w.name }),
      });

      if (!res.ok) throw new Error("Update fehlgeschlagen");

      // Lokalen Zustand aktualisieren
      setWorkouts((prev) =>
        prev.map((workout) => (workout.id === w.id ? { ...workout, name: w.name } : workout))
      );
    } catch (err) {
      console.error("Fehler beim Update", err);
    }
  }

  if (loading) return <p>Lade...</p>;
  if (error) return <p>Fehler: {error}</p>;

  const handleDoubleClick = (w: Workout) => (e: React.MouseEvent<HTMLTableCellElement>) => {
    e.stopPropagation();
    setEditingWorkout({ id: w.id, name: w.name });
  }
  const handleBlur = (id: string) => {
    if (editingWorkout?.name.trim() !== "") {
      updateWorkoutName(editingWorkout);
    }
    setEditingWorkout(null);
  }
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, id: string) => {
    if (e.key === "Enter") {
      e.preventDefault();
      (e.target as HTMLInputElement).blur(); // Trigger blur to save changes
    }
  }

  return (
    <>
      <Table>
        <TableCaption>A list of your recent invoices.</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead className="w-[100px]">Id</TableHead>
            <TableHead className="text-right">Name</TableHead>
            <TableHead className="text-right">Date</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {workouts.map((w) => (
            <TableRow onClick={() => onSelectWorkout(w.id)} key={w.id} data-state={getIsSelected(w.id) && "selected"}>
              <TableCell className="font-medium">{w.id}</TableCell>
              <TableCell onDoubleClick={handleDoubleClick(w)}>
                {editingWorkout?.id === w.id ? (
                  <input
                    value={editingWorkout.name}
                    autoFocus
                    onChange={(e) => setEditingWorkout({ ...editingWorkout, name: e.target.value })}
                    onBlur={() => handleBlur(w.id)}
                    onKeyDown={(e) => handleKeyDown(e, w.id)}
                    className="w-full border px-2 py-1 rounded"
                  />
                ) : (w.name)}
              </TableCell>
              <TableCell className="text-right">{new Date(Date.now()).toUTCString()}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </>
  );
}
