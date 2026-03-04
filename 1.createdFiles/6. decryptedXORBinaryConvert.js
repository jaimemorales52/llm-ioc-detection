// Importa la librería 'crypto-js' si estás en Node.js
const CryptoJS = require("crypto-js");

/**
 * @function BinaryConvert
 * @description Convert the decimal to binary.
 * @param {Integer} num - The input integer
 * @return {Integer} - Binary of num.
 * @see [BinaryConvert](https://www.programiz.com/javascript/examples/decimal-binary)
 * @example BinaryConvert(12) = 1100
 * @example BinaryConvert(12 + 2) = 1110
 */

const BinaryConvert = (num) => {
  let power = 1
  let binary = 0

  while (num) {
    const rem = num % 2
    num = Math.floor(num / 2)
    binary = rem * power + binary
    power *= 10
  }

  return binary
}

export { BinaryConvert }

function xorDecrypt(key, encryptedText) {
  let decryptedText = '';
  let encryptedTextBytes = Buffer.from(encryptedText, 'base64');
  for (let i = 0; i < encryptedTextBytes.length; i++) {
    decryptedText += String.fromCharCode(encryptedTextBytes[i] ^ key.charCodeAt(i % key.length));
  }
  return decryptedText;
}
