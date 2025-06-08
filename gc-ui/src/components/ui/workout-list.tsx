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
}
type WorkoutListProps = {
  selected: string | null;
  onSelectWorkout: (id: string) => void;
}
export function WorkoutList({ selected, onSelectWorkout }: WorkoutListProps) {
  const [workouts, setWorkouts] = useState<Workout[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

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

  if (loading) return <p>Lade...</p>;
  if (error) return <p>Fehler: {error}</p>;

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
              <TableCell>Workout</TableCell>
              <TableCell className="text-right">{new Date(Date.now()).toUTCString()}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </>
  );
}
