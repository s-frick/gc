import { describe, it, expect } from 'vitest';
import { lttbDownsample } from './sampler.ts'; // deine Implementierung
import type { WorkoutSample } from '@/App.tsx';

type Point = { x: number; y: number };

function generateSampleData(count: number): WorkoutSample[] {
  return Array.from({ length: count }, (_, i) => ({
    heartRate: 120 + Math.sin(i / 10) * 10,
    power: 200 + Math.sin(i / 5) * 50,
    cadence: 85 + Math.cos(i / 15) * 5,
    timestamp: i * 1000,
  }));
}

describe("lttbDownsample", () => {
  it("returns original data if threshold >= data length", () => {
    const data = generateSampleData(100);
    const result = lttbDownsample(data, 200);
    expect(result.length).toBe(100);
    expect(result).toEqual(data);
  });

  it("returns correct number of samples", () => {
    const data = generateSampleData(1000);
    const result = lttbDownsample(data, 100);
    expect(result.length).toBe(100);
  });

  it("retains first and last data points", () => {
    const data = generateSampleData(1000);
    const result = lttbDownsample(data, 100);
    expect(result[0]).toEqual(data[0]);
    expect(result[result.length - 1]).toEqual(data[data.length - 1]);
  });

  it("returns all points if threshold is 0", () => {
    const data = generateSampleData(500);
    const result = lttbDownsample(data, 0);
    expect(result.length).toBe(500);
  });

  it("does not contain undefined values", () => {
    const data = generateSampleData(300);
    const result = lttbDownsample(data, 50);
    expect(result.some((p) => p === undefined)).toBe(false);
  });
});
