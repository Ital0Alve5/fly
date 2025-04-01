import { defineStore } from 'pinia'

export const useMilesPurchaseStore = defineStore('milesPurchase', {
  state: () => ({
    miles: 10,
    pricePerMile: 5
  }),
  getters: {
    totalPrice: (state) => state.miles * state.pricePerMile
  },
  actions: {
    setMiles(value: number) {
      this.miles = value
    }
  }
})