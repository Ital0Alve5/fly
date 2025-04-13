import type { Airport } from '@/types/Airpoirt'

export const aipoirts: Airport[] = [
  {
    codigo: 'GRU',
    nome: 'São Paulo/Guarulhos International Airport',
    cidade: 'São Paulo',
    uf: 'SP',
  },
  { codigo: 'JFK', nome: 'John F. Kennedy International Airport', cidade: 'New York', uf: 'NY' },
  { codigo: 'CDG', nome: 'Charles de Gaulle Airport', cidade: 'Paris', uf: 'N/A' },
  { codigo: 'LAX', nome: 'Los Angeles International Airport', cidade: 'Los Angeles', uf: 'CA' },
  { codigo: 'LHR', nome: 'Heathrow Airport', cidade: 'London', uf: 'N/A' },
  { codigo: 'MEX', nome: 'Mexico City International Airport', cidade: 'Mexico City', uf: 'N/A' },
  { codigo: 'MAD', nome: 'Adolfo Suárez Madrid–Barajas Airport', cidade: 'Madrid', uf: 'N/A' },
  { codigo: 'NRT', nome: 'Narita International Airport', cidade: 'Tokyo', uf: 'N/A' },
  { codigo: 'FRA', nome: 'Frankfurt Airport', cidade: 'Frankfurt', uf: 'N/A' },
  { codigo: 'MIA', nome: 'Miami International Airport', cidade: 'Miami', uf: 'FL' },
  {
    codigo: 'GIG',
    nome: 'Rio de Janeiro/Galeão International Airport',
    cidade: 'Rio de Janeiro',
    uf: 'RJ',
  },
  {
    codigo: 'BSB',
    nome: 'Presidente Juscelino Kubitschek International Airport',
    cidade: 'Brasília',
    uf: 'DF',
  },
  { codigo: 'LIS', nome: 'Humberto Delgado Airport', cidade: 'Lisboa', uf: 'N/A' },
  { codigo: 'SDU', nome: 'Santos Dumont Airport', cidade: 'Rio de Janeiro', uf: 'RJ' },
  { codigo: 'CWB', nome: 'Afonso Pena International Airport', cidade: 'Curitiba', uf: 'PR' },
  { codigo: 'MAO', nome: 'Eduardo Gomes International Airport', cidade: 'Manaus', uf: 'AM' },
  { codigo: 'CGH', nome: 'Congonhas Airport', cidade: 'São Paulo', uf: 'SP' },
  { codigo: 'REC', nome: 'Guararapes International Airport', cidade: 'Recife', uf: 'PE' },
  {
    codigo: 'FOR',
    nome: 'Pinto Martins – Fortaleza International Airport',
    cidade: 'Fortaleza',
    uf: 'CE',
  },
  {
    codigo: 'EZE',
    nome: 'Ministro Pistarini International Airport',
    cidade: 'Buenos Aires',
    uf: 'N/A',
  },
]

export async function loadAllAirpoirts() {
  return aipoirts
}

export function loadCityByAirpoirt(codigo: string): string {
  const airpoirt = aipoirts.find((a) => a.codigo === codigo)

  if (!airpoirt) {
    throw new Error('Cidade não encontrada')
  }

  return airpoirt.cidade
}

export default { loadAllAirpoirts, loadCityByAirpoirt }
