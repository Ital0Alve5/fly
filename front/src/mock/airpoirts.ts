import type { Airport } from '@/types/Airpoirt'

export const aipoirts: Airport[] = [
  {
    code: 'GRU',
    name: 'São Paulo/Guarulhos International Airport',
    city: 'São Paulo',
    uf: 'SP',
  },
  {
    code: 'JFK',
    name: 'John F. Kennedy International Airport',
    city: 'New York',
    uf: 'NY',
  },
  {
    code: 'CDG',
    name: 'Charles de Gaulle Airport',
    city: 'Paris',
    uf: 'N/A',
  },
  {
    code: 'LAX',
    name: 'Los Angeles International Airport',
    city: 'Los Angeles',
    uf: 'CA',
  },
  {
    code: 'LHR',
    name: 'Heathrow Airport',
    city: 'London',
    uf: 'N/A',
  },
  {
    code: 'MEX',
    name: 'Mexico City International Airport',
    city: 'Mexico City',
    uf: 'N/A',
  },
  {
    code: 'MAD',
    name: 'Adolfo Suárez Madrid–Barajas Airport',
    city: 'Madrid',
    uf: 'N/A',
  },
  {
    code: 'NRT',
    name: 'Narita International Airport',
    city: 'Tokyo',
    uf: 'N/A',
  },
  {
    code: 'FRA',
    name: 'Frankfurt Airport',
    city: 'Frankfurt',
    uf: 'N/A',
  },
  {
    code: 'MIA',
    name: 'Miami International Airport',
    city: 'Miami',
    uf: 'FL',
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
