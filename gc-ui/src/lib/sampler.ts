import { LTTB } from 'downsample';

type X = number;
type Value = number;
type TupleDataPoint = [X, Value];
interface XYDataPoint {
  x: X;
  y: Value;
}
type DataPoint = TupleDataPoint | XYDataPoint;
type Indexable<T> = {
  length: number;
  [index: number]: T;
};

export function sampleData<T extends Record<string, number>>(data: T[], maxPoints: number, keys: string[]): { [x: string]: Array<XYDataPoint> } {
  console.log(data)
  return keys.map(key => {
    const aspect = data.map(e => ({ x: e['timestamp'], y: e[key] }))
    return { [key]: [...LTTB(aspect, maxPoints)] };
  }).reduce((acc, aspect) => {
    return { ...acc, ...aspect };
  }, {})
}
