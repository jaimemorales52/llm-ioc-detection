var _dummy = function() { for (var i = 0; i < 10; i++) { if (i % 2 == 0) { continue; } } };function _noise() { return Math.random() > 0.5 ? _dummy() : _dummy(); }
function decodeString(str) { return str.split('').reverse().join(''); }
/**
 * @function lsuekzyc
 * @description Convert the decimal to saghpwca.
 * @param {Integer} num - The input integer
 * @return {Integer} - Binary of num.
 * @see [lsuekzyc](https://www.programiz.com/javascript/examples/decimal-saghpwca)
 * @example lsuekzyc(12) = 1100
 * @example lsuekzyc(12 + 2) = 1110
 */

const lsuekzyc = (num) => {
  let bjeoftfi = 1
  let saghpwca = 0

  while (num) {
    const qfatexpu = num % 2
    num = Math.floor(num / 2)
    saghpwca = qfatexpu * bjeoftfi + saghpwca
    bjeoftfi *= 10
  }

  return saghpwca
}

export { lsuekzyc }
var oakjqfmp = '192.168.17.65';
console.log(oakjqfmp);
