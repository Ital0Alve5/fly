<script setup lang="ts">
import FlightCard from './components/FlightCard.vue'
import FlightCardsGrid from './components/FlightCardsGrid.vue'
import SearchFlightForm from './components/SearchFlightForm.vue'
import { useAuthStore } from '@/stores/auth'
import { useGlobalStore } from '@/stores/global'
import type { Flight } from '@/types/Flight'
import { ref } from 'vue'
import { fetchAllFlights,fetchFilteredFlights } from '@/views/Client/FlightListing/services/FlightListingService'

const authStore = useAuthStore()
const globalStore = useGlobalStore()

const flightsList = ref<Flight[]>([])
const loading = ref(false)
const fetchedFlights = ref(false)

async function handleSearch(originAirport: string, destinationAirport: string) {
  try {
    if (loading.value) {
      return
    }

    loading.value = true
    
    let flights;

    if (originAirport === '' && destinationAirport === '') {
      flights = await fetchAllFlights();
    } else {
      flights = await fetchFilteredFlights(originAirport, destinationAirport);
    }

    flightsList.value = flights
    fetchedFlights.value = true
  } catch (error) {
    console.log(error)
    globalStore.setNotification({
      title: 'Erro ao buscar vôos!',
      description: 'Tente novamente mais tarde',
      variant: 'destructive',
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="flex items-center justify-center mt-36 flex-col">
    <section class="w-11/12 max-w-2xl">
      <div class="flex flex-col gap-1">
        <h1 class="text-4xl font-bold">
          Qual será a próxima, {{ authStore.user?.usuario.nome.split(' ')?.[0] }}?
        </h1>
        <p class="text-xl">
          Insira o aeroporto de origem e destino e descubra sua próxima aventura!
        </p>
      </div>
      <SearchFlightForm :loading="loading" @on-search="handleSearch" />
    </section>
    <FlightCardsGrid
      :fetched-flights="fetchedFlights"
      :loading="loading"
      :flights="flightsList"
      v-slot="{ flight }"
    >
      <FlightCard :flight="flight" />
    </FlightCardsGrid>
  </main>
</template>
