<script setup lang="ts">
import FlightDetailsCard from '@/components/features/Flights/components/FlightDetailsCard.vue'
import FlightingPayment from '@/components/features/Flights/components/FlightingPayment.vue'
</script>
<template>
  <main class="flex items-center mt-36 flex-col">
    <section>
      <div class="flex flex-col">
        <h1 class="text-4xl font-bold">Detalhes do voo</h1>
      </div>
    </section>
    <section>
      <FlightDetailsCard />
      <FlightingPayment />
    </section>
  </main>
</template>
