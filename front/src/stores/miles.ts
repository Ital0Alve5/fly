import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getClientMiles } from '@/utils/getClientTotalMiles.ts'

export const useMilesStore = defineStore('miles', () => {
  const totalMiles = ref(0)
  const pricePerMile = ref(5)
  const currentCheckoutMiles = ref(0)

  const totalPrice = computed(() => currentCheckoutMiles.value * pricePerMile.value)

  async function refreshMiles() {
    try {
      const miles = await getClientMiles()
      if (typeof miles === 'number' && miles >= 0) {
        totalMiles.value = miles
      }
    } catch (e) {
      console.error('Erro ao atualizar milhas:', e)
    }
  }

  function setTotalMiles(value: number) {
    if (value < 0) return
    totalMiles.value = value
  }

  function setCurrentCheckoutMiles(value: number) {
    if (value < 0) return
    currentCheckoutMiles.value = value
  }

  refreshMiles()

  return {
    totalMiles,
    currentCheckoutMiles,
    pricePerMile,
    totalPrice,
    setTotalMiles,
    setCurrentCheckoutMiles,
    refreshMiles,
  }
})
