import { defineStore } from 'pinia'
import type { Notification } from '@/types/Notification'
import { ref } from 'vue'

export const useGlobalStore = defineStore('global', () => {
  const notification = ref<Notification | null>(null)

  function setNotification(data: Notification) {
    console.log(data)
    notification.value = data
  }

  return {
    notification,
    setNotification,
  }
})
