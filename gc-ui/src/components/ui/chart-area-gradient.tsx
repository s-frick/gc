"use client";

import { TrendingUp } from "lucide-react";
import { Area, AreaChart, CartesianGrid, XAxis } from "recharts";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  type ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { useEffect, useState } from "react";
import type { WorkoutSample } from "@/App";
import { sampleData } from "@/lib/sampler";
import { sma } from "@/lib/sma";
import { createSMA, SMA } from "downsample";

export const description = "An area chart with gradient fill";

const chartData = [
  { month: "January", desktop: 186, mobile: 80 },
  { month: "February", desktop: 305, mobile: 200 },
  { month: "March", desktop: 237, mobile: 120 },
  { month: "April", desktop: 73, mobile: 190 },
  { month: "May", desktop: 209, mobile: 130 },
  { month: "June", desktop: 214, mobile: 140 },
];

const samples = [
  { timestamp: 1, cadence: 80, heart_rate: 120 },
  { timestamp: 2, cadence: 85, heart_rate: 130 },
  { timestamp: 3, cadence: 75, heart_rate: 140 },
  { timestamp: 4, cadence: 80, heart_rate: 145 },
];

const chartConfig = {
  y: {
    label: "Herzfrequenz",
  },
  desktop: {
    label: "Desktop",
    color: "var(--chart-1)",
  },
  mobile: {
    label: "Mobile",
    color: "var(--chart-2)",
  },
} satisfies ChartConfig;

type ChartAreaGradientProps = {
  workoutId: string;
  workout: WorkoutSample[];
};

function mergeData(data: { [k: string]: Array<{ x: number; y: number; }> }) {
  console.time('merging data')
  const seenX = new Set<number>();
  const result = [];
  for (const key in data) {
    for (const p in data[key]) {
      const point = data[key][p];
      if (point.x && !seenX.has(point.x)) {
        let collector: { [k: string]: number } = { x: point.x };
        for (const k in data) {
          collector[k] = data[k][p].y
        }
        result.push(collector);
        seenX.add(point.x);
      } else if (point.x && seenX.has(point.x)) {
        result.filter(p => p.x === point.x)[0][key] = point.y;
      } else {
        console.warn('No point.x found for:', point);
      }
    }
  }
  const sorted = result.sort((a, b) => a.x - b.x);
  console.timeEnd('merging data')
  return sorted;
}

export function ChartAreaGradient({
  workout,
  workoutId,
}: ChartAreaGradientProps) {
  const [error, setError] = useState<string | null>(null);
  const sampleSize = workout.length;
  const downsampled = sampleData(workout, 2000, ["cadence", "heart_rate", "power"]);
  const merged = mergeData(downsampled);
  console.log('merged data',);

  // console.log("render with downsampled:", downsampled);
  // const ma = SMA(downsampled.heart_rate, 60);
  // console.log("render with ma:", ma.prototype);

  // console.log("render with workoutId:", workoutId);
  // console.log("render with workout:", workout);
  // console.log("render with ma:", ma);

  return (
    <Card>
      <CardHeader>
        <CardTitle>Bemer Cyclassics</CardTitle>
      </CardHeader>
      <CardDescription>Samples: {sampleSize}, Downsampled: {merged.length}</CardDescription>
      <CardContent>
        <ChartContainer config={chartConfig}>
          <AreaChart
            accessibilityLayer
            // data={workout.map(e => ({ x: e.timestamp, y: e.heart_rate }))}
            data={merged}
            // data={downsampled.cadence}
            margin={{
              left: 12,
              right: 12,
            }}
          >
            <CartesianGrid vertical={true} />
            <XAxis
              dataKey="x"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
            // tickFormatter={(value) => value.slice(0, 3)}
            />
            <ChartTooltip cursor={true} content={<ChartTooltipContent indicator="line" />} />
            <defs>
              <linearGradient id="fillDesktop" x1="0" y1="0" x2="0" y2="1">
                <stop
                  offset="10%"
                  stopColor="var(--color-desktop)"
                  stopOpacity={0.6}
                />
                <stop
                  offset="90%"
                  stopColor="var(--color-desktop)"
                  stopOpacity={0.01}
                />
              </linearGradient>
              <linearGradient id="fillMobile" x1="0" y1="0" x2="0" y2="1">
                <stop
                  offset="5%"
                  stopColor="var(--color-mobile)"
                  stopOpacity={0.8}
                />
                <stop
                  offset="95%"
                  stopColor="var(--color-mobile)"
                  stopOpacity={0.1}
                />
              </linearGradient>
            </defs>
            <Area
              dataKey="cadence"
              type="natural"
              fill="url(#fillMobile)"
              fillOpacity={0.4}
              stroke="var(--color-mobile)"
              stackId="a"
            />
            <Area
              dataKey="heart_rate"
              type="natural"
              fill="url(#fillDesktop)"
              fillOpacity={0.5}
              stroke="var(--color-desktop)"
              stackId="a"
            />
          </AreaChart>
        </ChartContainer>
      </CardContent>
      <CardFooter></CardFooter>
    </Card>
  );
}
