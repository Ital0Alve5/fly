import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useMilesStore = defineStore('miles', () => {
  const totalMiles = ref(0)
  const pricePerMile = ref(5)
  const currentCheckoutMiles = ref(0)

  const totalPrice = computed(() => currentCheckoutMiles.value * pricePerMile.value)

  function setTotalMiles(value: number) {
    if (value < 0) return

    totalMiles.value = value
  }

  function setCurrentCheckoutMiles(value: number) {
    if (value < 0) return

    currentCheckoutMiles.value = value
  }

  return {
    totalMiles,
    currentCheckoutMiles,
    pricePerMile,
    totalPrice,
    setTotalMiles,
    setCurrentCheckoutMiles,
  }
})
