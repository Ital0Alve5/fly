import { ref, watch } from 'vue'
import type { Ref } from 'vue'

export function useAddressByCep(cep: Ref<string>) {
  const street = ref('')
  const neighborhood = ref('')
  const city = ref('')
  const state = ref('')
  const number = ref('')
  const complement = ref('')

  function resetAddressFields() {
    street.value = ''
    neighborhood.value = ''
    city.value = ''
    state.value = ''
  }

  let debounceTimer: ReturnType<typeof setTimeout> | null = null

  watch(cep, (newCep) => {
    const cleanedCep = newCep.replace(/\D/g, '')
    if (cleanedCep.length !== 8) {
      resetAddressFields()
      return
    }

    if (debounceTimer) clearTimeout(debounceTimer)

    debounceTimer = setTimeout(async () => {
      try {
        const response = await fetch(`https://viacep.com.br/ws/${cleanedCep}/json/`)
        const data = await response.json()

        if (data.erro) {
          resetAddressFields()
          alert('CEP não encontrado.')
        } else {
          street.value = data.logradouro || ''
          neighborhood.value = data.bairro || ''
          city.value = data.localidade || ''
          state.value = data.uf || ''
        }
      } catch {
        resetAddressFields()
        alert('Erro ao buscar o CEP.')
      }
    }, 500)
  })

  return {
    street,
    neighborhood,
    city,
    state,
    resetAddressFields,
    number,
    complement,
  }
}
