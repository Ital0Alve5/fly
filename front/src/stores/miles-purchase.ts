import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useMilesPurchaseStore = defineStore('milesPurchase', () => {
  
  const miles = ref(10)
  const pricePerMile = ref(5)

  const totalPrice = computed(() => miles.value * pricePerMile.value)

  function setMiles(value: number) {
    miles.value = value
  }

  return {
    miles,
    pricePerMile,
    totalPrice,
    setMiles
  }
})
