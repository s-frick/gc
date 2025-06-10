export function sma(arr: Array<{ x: any, y: number }>, range: number) {
  if (!Array.isArray(arr)) {
    throw TypeError('expected first argument to be an array');
  }

  var num = range || arr.length;
  var res = [];
  var len = arr.length + 1;
  var idx = num - 1;
  while (++idx < len) {
    res.push(avg(arr, idx, num));
  }
  return res;
}

/**
 * Create an average for the specified range.
 *
 * ```js
 * console.log(avg([1, 2, 3, 4, 5, 6, 7, 8, 9], 5, 4));
 * //=> 3.5
 * ```
 */
function avg(arr: Array<{ x: any, y: number }>, idx: number, range: number) {
  const num = sum(arr.slice(idx - range, idx));
  return { x: num.x, y: num.y / range };
}

/**
 * Calculate the sum of an array.
 */
function sum(arr: Array<{ x: any, y: number }>) {
  var len = arr.length;
  var num = 0;
  while (len--) num += Number(arr[len].y);
  return { x: arr[arr.length - 1].x, y: num };
}

/**
 * Default format method.
 * @param  {Number} `n` Number to format.
 * @return {String} Formatted number.
 */

function toFixed(n: number) {
  return n.toFixed(2);
}

