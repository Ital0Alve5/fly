import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useMilesStore = defineStore('miles', () => {
  const miles = ref(10)
  const pricePerMile = ref(5)

  const totalPrice = computed(() => miles.value * pricePerMile.value)

  function setMiles(value: number) {
    if (value < 0) return

    miles.value = value
  }

  return {
    miles,
    pricePerMile,
    totalPrice,
    setMiles,
  }
})
