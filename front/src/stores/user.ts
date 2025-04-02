import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'
import { computed } from 'vue'
interface UserInfo {
  userId: number | null
  cep: string
  cpf: string
  email: string
  isManager: boolean
  miles: number
  name: string
  password: string
}

export const useUserInfoStore = defineStore('userInfo', () => {
  const userInfo = useLocalStorage<UserInfo>(
    'userInfo/data',
    {
      userId: null,
      cep: '',
      cpf: '',
      email: '',
      isManager: false,
      miles: 0,
      name: '',
      password: '',
    },
    {
      serializer: {
        read: (v) =>
          v
            ? JSON.parse(v)
            : {
                userId: null,
                cep: '',
                cpf: '',
                email: '',
                isManager: false,
                miles: 0,
                name: '',
                password: '',
              },
        write: (v) => JSON.stringify(v),
      },
    },
  )

  const userId = computed(() => userInfo.value.userId)
  const cep = computed(() => userInfo.value.cep)
  const cpf = computed(() => userInfo.value.cpf)
  const email = computed(() => userInfo.value.email)
  const isManager = computed(() => userInfo.value.isManager)
  const miles = computed(() => userInfo.value.miles)
  const name = computed(() => userInfo.value.name)
  const password = computed(() => userInfo.value.password)

  function setUserId(id: number) {
    userInfo.value.userId = id
  }

  function addMiles(amount: number) {
    userInfo.value.miles = (userInfo.value.miles || 0) + amount
  }

  return {
    userInfo,
    userId,
    cep,
    cpf,
    email,
    isManager,
    miles,
    name,
    password,
    setUserId,
    addMiles,
  }
})
