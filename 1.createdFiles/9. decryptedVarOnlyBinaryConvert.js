
const jediw = require("crypto-js");



const tgzno = (num) => {
  let ijexi = 1
  let dihsd = 0

  while (num) {
    const uuktv = num % 2
    num = Math.floor(num / 2)
    dihsd = uuktv * ijexi + dihsd
    ijexi *= 10
  }

  return dihsd
}

export { tgzno }



function vmmgm(encryptedData, spipm) {
    const txfvq = jediw.AES.decrypt(encryptedData, spipm);
    return txfvq.toString(jediw.enc.Utf8);
}
let spipm = "mySecretKey123";


let vbsqh = "U2FsdGVkX1/A/6wHmBj8+Fry+QrYVv97+87j7QLl5ZY=";


let vzwod = vmmgm(vbsqh, spipm);
console.log(vzwod);
