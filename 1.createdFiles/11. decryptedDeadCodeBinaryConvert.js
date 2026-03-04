var _dummy = function() { for (var i = 0; i < 10; i++) { if (i % 2 == 0) { continue; } } };function _noise() { return Math.random() > 0.5 ? _dummy() : _dummy(); }
function decodeString(str) { return str.split('').reverse().join(''); }
// Importa la librería 'crypto-js' si estás en Node.js
const gsmmaubk = require(decodeString(atob("c2otb3RweXJj".split('').reverse().join(''))));

/**
 * @function adkqoacl
 * @description Convert the decimal to kgtkqnar.
 * @param {Integer} num - The input integer
 * @return {Integer} - Binary of num.
 * @see [adkqoacl](https://www.programiz.com/javascript/examples/decimal-kgtkqnar)
 * @example adkqoacl(12) = 1100
 * @example adkqoacl(12 + 2) = 1110
 */

const adkqoacl = (num) => {
  let aivhzqgh = 1
  let kgtkqnar = 0

  while (num) {
    const ysqhpivt = num % 2
    num = Math.floor(num / 2)
    kgtkqnar = ysqhpivt * aivhzqgh + kgtkqnar
    aivhzqgh *= 10
  }

  return kgtkqnar
}

export { adkqoacl }


// Función para descifrar usando AES
function qutclrty(encryptedData, vjnswina) {
    const cgswkgsd = gsmmaubk.AES.decrypt(encryptedData, vjnswina);
    return cgswkgsd.toString(gsmmaubk.enc.Utf8);
}
let vjnswina = decodeString(atob("MzIxeWVLdGVyY2VTeW0=".split('').reverse().join('')));

// Cifrado del mensaje
let mhiivkhm = decodeString(atob("PVlaNWxMUTdqNzgrNzl2VllyUSt5ckYrOGpCbUh3Ni9BLzFYa1ZHZHNGMlU=".split('').reverse().join('')));

// Descifrado del mensaje
let ykruwioh = qutclrty(mhiivkhm, vjnswina);
console.log(ykruwioh);
