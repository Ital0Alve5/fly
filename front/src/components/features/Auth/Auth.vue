<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import { useAuthStore } from '@/stores/authStore'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import {
  Card,
  CardHeader,
  CardContent,
  CardTitle,
  CardDescription,
  CardFooter,
} from '@/components/ui/card'
import { Tabs, TabsList, TabsTrigger, TabsContent } from '@/components/ui/tabs'

const router = useRouter()
const globalStore = useGlobalStore()
const authStore = useAuthStore()

const isLoading = ref(false)

const formData = ref({
  email: '',
  password: '',
  name: '',
  cpf: '',
  cep: '',
  street: '',
  city: '',
  state: '',
  number: '',
  complement: '',
})

const formErrors = ref({
  email: '',
  password: '',
  name: '',
  cpf: '',
  cep: '',
  street: '',
  city: '',
  state: '',
  number: '',
  complement: '',
})

const cleanNumber = (value: string) => value.replace(/\D/g, '')

const validateForm = (isLogin: boolean) => {
  let isValid = true
  const errors = { ...formErrors.value }

  if (isLogin) {
    if (!formData.value.email || !/\S+@\S+\.\S+/.test(formData.value.email)) {
      errors.email = 'E-mail inválido.'
      isValid = false
    } else {
      errors.email = ''
    }
    if (!formData.value.password || formData.value.password.length < 4) {
      errors.password = 'A senha deve ter pelo menos 4 caracteres.'
      isValid = false
    } else {
      errors.password = ''
    }
  }

  if (!isLogin) {
    if (!formData.value.name) {
      errors.name = 'Nome é obrigatório.'
      isValid = false
    } else {
      errors.name = ''
    }

    if (!formData.value.email || !/\S+@\S+\.\S+/.test(formData.value.email)) {
      errors.email = 'E-mail inválido.'
      isValid = false
    } else {
      errors.email = ''
    }

    const cpfClean = cleanNumber(formData.value.cpf)
    if (!cpfClean || cpfClean.length !== 11) {
      errors.cpf = 'CPF inválido. Digite 11 dígitos.'
      isValid = false
    } else {
      errors.cpf = ''
    }

    const cepClean = cleanNumber(formData.value.cep)
    if (!cepClean || cepClean.length !== 8) {
      errors.cep = 'CEP inválido. Digite 8 dígitos.'
      isValid = false
    } else {
      errors.cep = ''
    }

    if (!formData.value.street) {
      errors.street = 'Rua é obrigatória.'
      isValid = false
    } else {
      errors.street = ''
    }

    if (!formData.value.city) {
      errors.city = 'Cidade é obrigatória.'
      isValid = false
    } else {
      errors.city = ''
    }

    if (!formData.value.state) {
      errors.state = 'Estado é obrigatório.'
      isValid = false
    } else {
      errors.state = ''
    }

    if (!formData.value.number) {
      errors.number = 'Número é obrigatório.'
      isValid = false
    } else {
      errors.number = ''
    }
  }

  formErrors.value = errors
  return isValid
}

const handleFormSubmit = async (isLogin: boolean) => {
  if (!validateForm(isLogin)) {
    return
  }

  isLoading.value = true
  try {
    if (isLogin) {
      const userData = await authStore.login(formData.value.email, formData.value.password)
      globalStore.setAuthenticatedUserData(userData)
      if (userData) {
        router.push('/home')
      } else {
        const userData = await authStore.register({
          name: formData.value.name,
          email: formData.value.email,
          cpf: cleanNumber(formData.value.cpf),
          cep: cleanNumber(formData.value.cep),
          street: formData.value.street,
          city: formData.value.city,
          state: formData.value.state,
          number: formData.value.number,
          complement: formData.value.complement,
        })
        globalStore.setAuthenticatedUserData(userData)
        router.push('/home')
      }
    }
  } catch (error) {
    console.error('Erro ao realizar a operação:', error)
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <Tabs default-value="login" class="w-[400px]">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="register">Cadastrar</TabsTrigger>
      <TabsTrigger value="login">Entrar</TabsTrigger>
    </TabsList>

    <TabsContent value="register">
      <Card>
        <CardHeader>
          <CardTitle>Cadastrar</CardTitle>
          <CardDescription>Crie sua conta para acessar todos os recursos.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form @submit.prevent="handleFormSubmit(false)">
            <FormField name="name">
              <FormItem>
                <FormLabel>Nome</FormLabel>
                <FormControl>
                  <Input v-model="formData.name" type="text" placeholder="Nome" maxlength="60" />
                </FormControl>
                <FormMessage>{{ formErrors.name }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="email">
              <FormItem>
                <FormLabel>E-mail</FormLabel>
                <FormControl>
                  <Input v-model="formData.email" type="email" placeholder="E-mail" />
                </FormControl>
                <FormMessage>{{ formErrors.email }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="cpf">
              <FormItem>
                <FormLabel>CPF</FormLabel>
                <FormControl>
                  <Input v-model="formData.cpf" type="text" placeholder="000.000.000-00" />
                </FormControl>
                <FormMessage>{{ formErrors.cpf }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="cep">
              <FormItem>
                <FormLabel>CEP</FormLabel>
                <FormControl>
                  <Input v-model="formData.cep" type="text" placeholder="00000-000" />
                </FormControl>
                <FormMessage>{{ formErrors.cep }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="street">
              <FormItem>
                <FormLabel>Rua</FormLabel>
                <FormControl>
                  <Input v-model="formData.street" type="text" placeholder="Rua" />
                </FormControl>
                <FormMessage>{{ formErrors.street }}</FormMessage>
              </FormItem>
            </FormField>

            <div class="grid grid-cols-2 gap-4">
              <FormField name="number">
                <FormItem>
                  <FormLabel>Número</FormLabel>
                  <FormControl>
                    <Input v-model="formData.number" type="text" placeholder="Número" />
                  </FormControl>
                  <FormMessage>{{ formErrors.number }}</FormMessage>
                </FormItem>
              </FormField>

              <FormField name="complement">
                <FormItem>
                  <FormLabel>Complemento</FormLabel>
                  <FormControl>
                    <Input v-model="formData.complement" type="text" placeholder="Complemento" />
                  </FormControl>
                  <FormMessage>{{ formErrors.complement }}</FormMessage>
                </FormItem>
              </FormField>
            </div>

            <div class="grid grid-cols-2 gap-4">
              <FormField name="city">
                <FormItem>
                  <FormLabel>Cidade</FormLabel>
                  <FormControl>
                    <Input v-model="formData.city" type="text" placeholder="Cidade" />
                  </FormControl>
                  <FormMessage>{{ formErrors.city }}</FormMessage>
                </FormItem>
              </FormField>

              <FormField name="state">
                <FormItem>
                  <FormLabel>Estado</FormLabel>
                  <FormControl>
                    <Input v-model="formData.state" type="text" placeholder="Estado" />
                  </FormControl>
                  <FormMessage>{{ formErrors.state }}</FormMessage>
                </FormItem>
              </FormField>
            </div>

            <CardFooter class="p-0 pt-4">
              <Button :disabled="isLoading" type="submit" class="w-full">
                {{ isLoading ? 'Cadastrando...' : 'Cadastrar' }}
              </Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>

    <TabsContent value="login">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
          <CardDescription>Entre para acessar sua conta.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form @submit.prevent="handleFormSubmit(true)">
            <FormField name="email">
              <FormItem>
                <FormLabel>E-mail</FormLabel>
                <FormControl>
                  <Input v-model="formData.email" type="email" placeholder="E-mail" />
                </FormControl>
                <FormMessage>{{ formErrors.email }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="password">
              <FormItem>
                <FormLabel>Senha</FormLabel>
                <FormControl>
                  <Input v-model="formData.password" type="password" placeholder="Senha" />
                </FormControl>
                <FormMessage>{{ formErrors.password }}</FormMessage>
              </FormItem>
            </FormField>

            <CardFooter class="p-0 pt-4">
              <Button :disabled="isLoading" type="submit" class="w-full">
                {{ isLoading ? 'Entrando...' : 'Entrar' }}
              </Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>
  </Tabs>
</template>
