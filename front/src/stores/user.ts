import { defineStore } from 'pinia'

export const useUserInfoStore = defineStore('userInfo', {
  state: () => ({
    userId: null as number | null
  }),
  actions: {
    setUserId(id: number) {
      this.userId = id
    }
  }
})