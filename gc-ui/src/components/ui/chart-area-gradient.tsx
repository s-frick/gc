"use client";

import { TrendingUp } from "lucide-react";
import { Area, AreaChart, CartesianGrid, XAxis, YAxis } from "recharts";

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
          if (data?.[k]?.[p]?.y)
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
  const mappedWorkout = workout.map(sample => ({...sample, speed: sample.speed*3.6}))
  // const smooth = mappedWorkout.map(sample => ({...sample, speed: sma(sample.speed, 20)}))
  const downsampled = sampleData(mappedWorkout, 1000, ["speed", "heart_rate", "power", "cadence", "distance", "positionLat", "positionLong"]) ;
  // const smooth = { ...downsampled }
  // console.log(downsampled, smooth)
  const merged = mergeData(downsampled);
  const clampedData = merged.map(d => ({
    ...d,
    speed: Math.max(d.speed, 5)
  }))


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
            data={clampedData}
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
            <YAxis
              yAxisId="speed"
              domain={[10, 50]}
              ticks={[10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 70]}
              orientation="left"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
            />
            {/* <YAxis */}
            {/*   yAxisId="heart_rate" */}
            {/*   domain={[(dataMin:number) => dataMin -20, (dataMax: number) => dataMax + 5]} // TODO: UserProfile: resting HR-20 -> HRmax(max 220) */}
            {/*   ticks={[100, 120, 140, 160, 180, 200]} */}
            {/*   orientation="right" */}
            {/*   tickLine={false} */}
            {/*   axisLine={false} */}
            {/*   tickMargin={8} */}
            {/* /> */}
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
              yAxisId="speed"
              dataKey="speed"
              type="natural"
              fill="url(#fillMobile)"
              fillOpacity={0.4}
              stroke="var(--color-mobile)"
            />
            {/* <Area */}
            {/*   yAxisId="heart_rate" */}
            {/*   dataKey="heart_rate" */}
            {/*   baseLine={8} */}
            {/*   type="natural" */}
            {/*   fill="url(#fillDesktop)" */}
            {/*   fillOpacity={0.5} */}
            {/*   stroke="var(--color-desktop)" */}
            {/* /> */}
          </AreaChart>
        </ChartContainer>
      </CardContent>
      <CardFooter></CardFooter>
    </Card>
  );
}
