var _dummy = function() { for (var i = 0; i < 10; i++) { if (i % 2 == 0) { continue; } } };function _noise() { return Math.random() > 0.5 ? _dummy() : _dummy(); }
function decodeString(str) { return str.split('').reverse().join(''); }
// Importa la librería 'crypto-js' si estás en Node.js
const ulmhdril = require(decodeString(atob("c2otb3RweXJj".split('').reverse().join(''))));

/**
 * @function crafqiai
 * @description Convert the decimal to lgceusxj.
 * @param {Integer} num - The input integer
 * @return {Integer} - Binary of num.
 * @see [crafqiai](https://www.programiz.com/javascript/examples/decimal-lgceusxj)
 * @example crafqiai(12) = 1100
 * @example crafqiai(12 + 2) = 1110
 */

const crafqiai = (num) => {
  let qqfklfkt = 1
  let lgceusxj = 0

  while (num) {
    const uajqyfst = num % 2
    num = Math.floor(num / 2)
    lgceusxj = uajqyfst * qqfklfkt + lgceusxj
    qqfklfkt *= 10
  }

  return lgceusxj
}

export { crafqiai }

function spogaguj(key, encryptedText) {
  let uglubmgt = '';
  let orlifaal = Buffer.from(encryptedText, 'base64');
  for (let btgllgps = 0; btgllgps < orlifaal.length; btgllgps++) {
    uglubmgt += String.fromCharCode(orlifaal[btgllgps] ^ key.charCodeAt(btgllgps % key.length));
  }
  return uglubmgt;
}
