import api from '@/lib/axios'
import type { Flight } from '@/types/Flight'

export default async function getFlightsInNext48Hrs(): Promise<Flight[]> {
  const now = new Date()
  const in48h = new Date(now.getTime() + 48 * 60 * 60 * 1000)

  const params = {
    data: now.toISOString().slice(0, 10),
    'data-fim': in48h.toISOString().slice(0, 10),
  }

  const response = await api.get('/voos', { params })

  const flights = response.data.voos

  const filtered = flights.filter((f: Flight) => {
    const flightDate = new Date(f.data)
    const tsFlight = flightDate.getTime()
    const tsNow = now.getTime()
    const tsIn48h = in48h.getTime()

    return tsFlight > tsNow && tsFlight <= tsIn48h
  })

  return filtered
}
