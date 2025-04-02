<script setup lang="ts">
import FlightCard from '@/components/features/Flights/components/FlightCard.vue'
import FlightCardsGrid from '@/components/features/Flights/components/FlightCardsGrid.vue'
import SearchFlightForm from '@/components/features/Flights/components/SearchFlightForm.vue'
import { searchFlights, type Flight } from '@/mock/flight'
import { useAuthStore } from '@/stores/auth'
import { useGlobalStore } from '@/stores/global'
import { ref } from 'vue'

const authStore = useAuthStore()
const globalStore = useGlobalStore()

const flightsList = ref<Flight[]>([])
const loading = ref(false)

async function handleSearch(originAipoirt: string, destinationAirpoirt: string) {
  try {
    if (loading.value) {
      return
    }

    loading.value = true

    const flights = await searchFlights(originAipoirt, destinationAirpoirt)
    flightsList.value = flights
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
          Qual será a próxima, {{ authStore.user?.name.split(' ')?.[0] }}?
        </h1>
        <p class="text-xl">
          Insira o aeroporto de origem e destino e descubra sua próxima aventura!
        </p>
      </div>
      <SearchFlightForm :loading @on-search="handleSearch" />
    </section>
    <FlightCardsGrid :loading :flights="flightsList" v-slot="{ flight }">
      <FlightCard :flight="flight" />
    </FlightCardsGrid>
  </main>
</template>
