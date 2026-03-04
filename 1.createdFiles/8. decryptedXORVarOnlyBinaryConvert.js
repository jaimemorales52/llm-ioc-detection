
const fhdzq = require("crypto-js");



const abcod = (num) => {
  let vigxg = 1
  let xfvqb = 0

  while (num) {
    const anhwz = num % 2
    num = Math.floor(num / 2)
    xfvqb = anhwz * vigxg + xfvqb
    vigxg *= 10
  }

  return xfvqb
}

export { abcod }

function wbnsr(key, encryptedText) {
  let liimx = '';
  let cxtlr = Buffer.from(encryptedText, 'base64');
  for (let nlcnr = 0; nlcnr < cxtlr.length; nlcnr++) {
    liimx += String.fromCharCode(cxtlr[nlcnr] ^ key.charCodeAt(nlcnr % key.length));
  }
  return liimx;
}
