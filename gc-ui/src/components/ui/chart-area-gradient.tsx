"use client";

import { Area, AreaChart, CartesianGrid, XAxis, YAxis } from "recharts";

import {
  type ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import type { WorkoutSample } from "@/App";
import { lttbDownsample } from "@/lib/sampler";
import type { CategoricalChartState } from "recharts/types/chart/types";

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
  heart_rate: {
    label: "Herzfrequenz",
    color: "var(--chart-1)",
  },
  speed: {
    label: "Geschwindigkeit",
    color: "var(--chart-2)",
  },
  power: {
    label: "Leistung",
    color: "var(--chart-3)",
  },
  cadence: {
    label: "Trittfrequenz",
    color: "var(--chart-5)",
  },
} satisfies ChartConfig;

type ChartAreaGradientProps = {
  workoutId: string;
  workout: WorkoutSample[];
  show: {
    power: boolean;
    cadence: boolean;
    heart_rate: boolean;
    speed: boolean;
  };
  onHoverGps: (gps: [number, number] | null) => void;
};



export function ChartAreaGradient({
  workout, show, onHoverGps
}: ChartAreaGradientProps) {
  console.time('lttb')
  let res = lttbDownsample(workout, 1500, 'heart_rate').map(s => ({ ...s, speed: (s?.speed ?? 0) * 3.6 } as WorkoutSample))
  console.timeEnd('lttb')
  // res = lttbDownsample(res, 1000, 'heart_rate')

  const handleMouseMove = (state: CategoricalChartState) => {
    if (state && state.activeTooltipIndex != null) {
      const sample = res[state.activeTooltipIndex];
      if (sample.positionLat != null && sample.positionLong != null) {
        onHoverGps?.([sample.positionLat, sample.positionLong]);
      } else {
        onHoverGps?.(null);
      }
    } else {
      onHoverGps?.(null);
    }
  }



  return (
    <ChartContainer config={chartConfig}>
      <AreaChart
        accessibilityLayer
        data={res}
        onMouseMove={handleMouseMove}
        onMouseLeave={() => onHoverGps?.(null)}
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
          orientation="bottom"
        />
        <XAxis
          dataKey="distance"
          tickLine={false}
          axisLine={false}
          tickMargin={8}
        />
        <YAxis
          yAxisId="speed"
          orientation="left"
          tickLine={false}
          axisLine={false}
          tickCount={8}
          label={{
            value: 'Geschwindigkeit km/h',
            angle: -90,
            position: 'insideLeft'
          }}
        />
        <YAxis
          yAxisId="heart_rate"
          orientation="right"
          tickLine={false}
          axisLine={false}
          tickCount={8}
          label={{
            value: 'Herzfrequenz bpm',
            angle: -90,
            position: 'insideRight'
          }}
        />
        <YAxis
          yAxisId="cadence"
          orientation="right"
          tickLine={false}
          axisLine={false}
          tickMargin={8}
          label={{
            value: 'Trittfrequenz',
            angle: -90,
            position: 'insideRight'
          }}
        />
        <YAxis
          yAxisId="power"
          orientation="left"
          tickLine={false}
          axisLine={false}
          tickMargin={8}
          label={{
            value: 'Leistung W',
            angle: -90,
            position: 'insideLeft'
          }}
        />
        <ChartTooltip cursor={true} content={<ChartTooltipContent indicator="line" />} />
        <defs>
          <linearGradient id="fillHeartrate" x1="0" y1="0" x2="0" y2="1">
            <stop
              offset="10%"
              stopColor="var(--color-heart_rate)"
              stopOpacity={0.6}
            />
            <stop
              offset="90%"
              stopColor="var(--color-heart_rate)"
              stopOpacity={0.01}
            />
          </linearGradient>
          <linearGradient id="fillSpeed" x1="0" y1="0" x2="0" y2="1">
            <stop
              offset="10%"
              stopColor="var(--color-speed)"
              stopOpacity={0.6}
            />
            <stop
              offset="90%"
              stopColor="var(--color-speed)"
              stopOpacity={0.01}
            />
          </linearGradient>
          <linearGradient id="fillCadence" x1="0" y1="0" x2="0" y2="1">
            <stop
              offset="10%"
              stopColor="var(--color-cadence)"
              stopOpacity={0.6}
            />
            <stop
              offset="90%"
              stopColor="var(--color-cadence)"
              stopOpacity={0.01}
            />
          </linearGradient>
          <linearGradient id="fillPower" x1="0" y1="0" x2="0" y2="1">
            <stop
              offset="10%"
              stopColor="var(--color-power)"
              stopOpacity={0.6}
            />
            <stop
              offset="90%"
              stopColor="var(--color-power)"
              stopOpacity={0.01}
            />
          </linearGradient>
        </defs>
        {show.heart_rate && (
          <Area
            dataKey="heart_rate"
            yAxisId="heart_rate"
            type="natural"
            fill="url(#fillHeartrate)"
            fillOpacity={0.5}
            stroke="var(--color-heart_rate)"
            stackId="a"
          />
        )}
        {show.speed && (
          <Area
            dataKey="speed"
            yAxisId="speed"
            type="natural"
            fill="url(#fillSpeed)"
            fillOpacity={0.4}
            stroke="var(--color-speed)"
            stackId="a"
          />
        )}
        {show.cadence && (
          <Area
            dataKey="cadence"
            yAxisId="cadence"
            type="natural"
            fill="url(#fillCadence)"
            fillOpacity={0.4}
            stroke="var(--color-cadence)"
            stackId="a"
          />
        )}
        {show.power && (
          <Area
            dataKey="power"
            yAxisId="power"
            type="natural"
            fill="url(#fillPower)"
            fillOpacity={0.4}
            stroke="var(--color-power)"
            stackId="a"
          />
        )}
      </AreaChart>
    </ChartContainer>
  );
}
