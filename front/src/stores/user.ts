import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserInfoStore = defineStore('userInfo', () => {

  const userId = ref<number | null>(null)

  function setUserId(id: number) {
    userId.value = id
  }

  return {
    userId,
    setUserId
  }
})