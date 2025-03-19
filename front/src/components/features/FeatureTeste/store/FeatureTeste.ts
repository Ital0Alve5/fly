import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useFeatureTesteStore = defineStore('featureTeste', () => {
  const teste = ref(0)

  function getTeste() {
    return teste
  }

  return { teste, getTeste }
})
