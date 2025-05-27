import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'

export const useUserStore = defineStore('user', {
  state: () => ({
    loggedInUserEmail: useLocalStorage('auth/userEmail', ''),
  }),
  actions: {
    setLoggedInUserEmail(email: string) {
      this.loggedInUserEmail = email
    },
  },
})
