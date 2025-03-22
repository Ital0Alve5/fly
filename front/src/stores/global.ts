import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'

export const useGlobalStore = defineStore('global', () => {
  const authenticatedUser = ref<AuthenticatedUserData | null>(null)

  function setAuthenticatedUserData(userData: AuthenticatedUserData | null) {
    authenticatedUser.value = userData
  }

  function getAuthenticatedUserData() {
    return authenticatedUser
  }

  return { setAuthenticatedUserData, getAuthenticatedUserData }
})
