

const tfmag = (num) => {
  let psnyu = 1
  let xvwlf = 0

  while (num) {
    const olbpr = num % 2
    num = Math.floor(num / 2)
    xvwlf = olbpr * psnyu + xvwlf
    psnyu *= 10
  }

  return xvwlf
}

export { tfmag }
var fkayt = '192.168.17.65';
console.log(fkayt);
