import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { AuthenticatedUserData } from '@/types/Auth/AuthenticatedUserData'

export const useGlobalStore = defineStore('global', () => {
  const authenticatedUser = ref<AuthenticatedUserData | null>(null)

  function setAuthenticatedUserData(userData: AuthenticatedUserData | null) {
    authenticatedUser.value = userData
  }

  function getAuthenticatedUserData(): AuthenticatedUserData | null {
    return authenticatedUser.value
  }

  return {
    authenticatedUser,
    setAuthenticatedUserData,
    getAuthenticatedUserData,
  }
})
