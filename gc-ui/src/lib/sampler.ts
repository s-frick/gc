import type { WorkoutSample } from "@/App";

export type DataPoint2 = {
  speed: number;
  distance: number;
  heartRate: number;
  power: number;
  cadence: number;
  timestamp: number;
  // int    positionLat,
  // int    positionLong
};

export function lttbDownsample(data: WorkoutSample[], threshold: number, key: keyof WorkoutSample): WorkoutSample[] {
  const dataLength = data.length;
  if (threshold >= dataLength || threshold === 0) return data;

  const sampled: WorkoutSample[] = [];
  const bucketSize = (dataLength - 2) / (threshold - 2);

  let a = 0;
  sampled.push(data[a]);

  for (let i = 0; i < threshold - 2; i++) {
    const start = Math.floor((i + 1) * bucketSize) + 1;
    const end = Math.floor((i + 2) * bucketSize) + 1;
    const range = data.slice(start, Math.min(end, dataLength));

    // Durchschnittswerte im Bucket
    const avgX = range.reduce((sum, p) => sum + p.timestamp, 0) / range.length;
    const avgY = range.reduce((sum, p) => sum + (p?.[key] ?? 0), 0) / range.length;

    const pointA = data[a];
    const rangeOffs = Math.floor(i * bucketSize) + 1;
    const rangeTo = Math.floor((i + 1) * bucketSize) + 1;
    const bucket = data.slice(rangeOffs, Math.min(rangeTo, dataLength));

    let maxArea = -1;
    let nextA = 0;

    for (let j = 0; j < bucket.length; j++) {
      const point = bucket[j];

      const area = Math.abs(
        (pointA.timestamp - avgX) * ((point?.[key] ?? 0) - (pointA?.[key] ?? 0)) -
        (pointA.timestamp - point.timestamp) * (avgY - (pointA?.[key] ?? 0))
      ) * 0.5;

      if (area > maxArea) {
        maxArea = area;
        nextA = rangeOffs + j;
      }
    }

    sampled.push(data[nextA]);
    a = nextA;
  }

  sampled.push(data[dataLength - 1]);
  return sampled;
}
