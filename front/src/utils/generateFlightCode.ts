export function generateFlightCode(): string {
  const numeroAleatorio = Math.floor(Math.random() * 10000)

  const algarismos = numeroAleatorio.toString().padStart(4, '0')

  return `TADS${algarismos}`
}
