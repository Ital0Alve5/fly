const generatedCodes = new Set()

function generateRandomCode() {
  const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const digits = '0123456789'

  const randomLetters = () => {
    let result = ''
    for (let i = 0; i < 3; i++) {
      result += letters[Math.floor(Math.random() * letters.length)]
    }
    return result
  }

  const randomDigits = () => {
    let result = ''
    for (let i = 0; i < 3; i++) {
      result += digits[Math.floor(Math.random() * digits.length)]
    }
    return result
  }

  return randomLetters() + randomDigits()
}

export function generateUniqueCode() {
  let code
  do {
    code = generateRandomCode()
  } while (generatedCodes.has(code))
  generatedCodes.add(code)
  return code
}
