import { useState, ChangeEvent, DragEvent } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";

export function FileUploader() {
  const [file, setFile] = useState<File | null>(null);
  const [dragActive, setDragActive] = useState(false);

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleDrop = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files?.[0]) {
      setFile(e.dataTransfer.files[0]);
    }
  };

  const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setDragActive(true);
  };

  const handleDragLeave = () => setDragActive(false);

  const handleUpload = () => {
    if (!file) return;
    const formData = new FormData();
    formData.append("file", file);

    fetch("/api/v1/upload-workout", {
      method: "POST",
      body: formData,
    })
      .then((res) => res.json())
      .then((data) => {
        console.log("Upload success:", data);
      })
      .catch((err) => {
        console.error("Upload error:", err);
      });
  };

  return (
    <Card
      className={`p-4 border-dashed border-2 ${
        dragActive ? "border-blue-500 bg-blue-50" : "border-gray-300"
      }`}
      onDrop={handleDrop}
      onDragOver={handleDragOver}
      onDragLeave={handleDragLeave}
    >
      <CardContent className="flex flex-col items-center gap-4">
        <Label htmlFor="file-upload">Datei auswählen oder hierher ziehen</Label>
        <Input id="file-upload" type="file" onChange={handleFileChange} />
        {file && <p className="text-sm text-gray-600">Ausgewählt: {file.name}</p>}
        <Button onClick={handleUpload} disabled={!file}>
          Hochladen
        </Button>
      </CardContent>
    </Card>
  );
}

