import type { Airport } from '@/types/Airpoirt'

export const aipoirts: Airport[] = [
  { code: 'GRU', name: 'São Paulo/Guarulhos International Airport', city: 'São Paulo', uf: 'SP' },
  { code: 'JFK', name: 'John F. Kennedy International Airport', city: 'New York', uf: 'NY' },
  { code: 'CDG', name: 'Charles de Gaulle Airport', city: 'Paris', uf: 'N/A' },
  { code: 'LAX', name: 'Los Angeles International Airport', city: 'Los Angeles', uf: 'CA' },
  { code: 'LHR', name: 'Heathrow Airport', city: 'London', uf: 'N/A' },
  { code: 'MEX', name: 'Mexico City International Airport', city: 'Mexico City', uf: 'N/A' },
  { code: 'MAD', name: 'Adolfo Suárez Madrid–Barajas Airport', city: 'Madrid', uf: 'N/A' },
  { code: 'NRT', name: 'Narita International Airport', city: 'Tokyo', uf: 'N/A' },
  { code: 'FRA', name: 'Frankfurt Airport', city: 'Frankfurt', uf: 'N/A' },
  { code: 'MIA', name: 'Miami International Airport', city: 'Miami', uf: 'FL' },
  {
    code: 'GIG',
    name: 'Rio de Janeiro/Galeão International Airport',
    city: 'Rio de Janeiro',
    uf: 'RJ',
  },
  {
    code: 'BSB',
    name: 'Presidente Juscelino Kubitschek International Airport',
    city: 'Brasília',
    uf: 'DF',
  },
  { code: 'LIS', name: 'Humberto Delgado Airport', city: 'Lisboa', uf: 'N/A' },
  { code: 'SDU', name: 'Santos Dumont Airport', city: 'Rio de Janeiro', uf: 'RJ' },
  { code: 'CWB', name: 'Afonso Pena International Airport', city: 'Curitiba', uf: 'PR' },
  { code: 'MAO', name: 'Eduardo Gomes International Airport', city: 'Manaus', uf: 'AM' },
  { code: 'CGH', name: 'Congonhas Airport', city: 'São Paulo', uf: 'SP' },
  { code: 'REC', name: 'Guararapes International Airport', city: 'Recife', uf: 'PE' },
  {
    code: 'FOR',
    name: 'Pinto Martins – Fortaleza International Airport',
    city: 'Fortaleza',
    uf: 'CE',
  },
  {
    code: 'EZE',
    name: 'Ministro Pistarini International Airport',
    city: 'Buenos Aires',
    uf: 'N/A',
  },
]

export async function loadAllAirpoirts() {
  return aipoirts
}

export function loadCityByAirpoirt(code: string): string {
  const airpoirt = aipoirts.find((a) => a.code === code)

  if (!airpoirt) {
    throw new Error('Cidade não encontrada')
  }

  return airpoirt.city
}

export default { loadAllAirpoirts, loadCityByAirpoirt }
