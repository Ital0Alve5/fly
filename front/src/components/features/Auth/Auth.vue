<script setup lang="ts">
import { useForm, useField } from 'vee-validate'
import { useAuthStore } from '@/stores/authStore'
import { useGlobalStore } from '@/stores/global'
import { useRouter } from 'vue-router'
import { ref, watch } from 'vue'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '../../ui/card'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '../../ui/form'
import { Input } from '../../ui/input'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../../ui/tabs'
import { Button } from '../../ui/button'

const authStore = useAuthStore()
const globalStore = useGlobalStore()
const router = useRouter()
const isLogin = ref(true)

const validateRequired = (value: string) => (!value ? 'Campo obrigatório' : true)
const validateEmail = (value: string) =>
  /^[^@]+@[^@]+\.[^@]+$/.test(value) ? true : 'E-mail inválido'
const validatePassword = (value: string) =>
  value && value.length === 4 && /^\d{4}$/.test(value) ? true : 'Senha deve ter 4 dígitos'

const { handleSubmit } = useForm()
const name = useField('name', validateRequired)
const email = useField('email', validateEmail)
const cpf = useField('cpf', validateRequired)
const cep = useField('cep', validateRequired)
const password = useField('password', validatePassword)

const street = ref('')
const neighborhood = ref('')
const city = ref('')
const state = ref('')

watch(cep.value, async (newCep) => {
  const cleanedCep = newCep.replace(/\D/g, '')
  if (cleanedCep.length === 8) {
    try {
      const response = await fetch(`https://viacep.com.br/ws/${cleanedCep}/json/`)
      const data = await response.json()
      if (!data.erro) {
        street.value = data.logradouro || ''
        neighborhood.value = data.bairro || ''
        city.value = data.localidade || ''
        state.value = data.uf || ''
      } else {
        street.value = ''
        neighborhood.value = ''
        city.value = ''
        state.value = ''
        alert('CEP não encontrado.')
      }
    } catch {
      alert('Erro ao buscar o CEP.')
    }
  }
})

const onSubmit = handleSubmit(async (values) => {
  try {
    if (isLogin.value) {
      const userData = await authStore.login(values.email, values.password)
      if (userData) {
        globalStore.setAuthenticatedUserData(userData)
        if (userData.isManager) {
          router.push('/admTeste')
        } else {
          router.push('/reservas')
        }
      } else {
        alert('Login inválido')
      }
    } else {
      const newUserData = {
        name: values.name,
        email: values.email,
        cpf: values.cpf,
        cep: values.cep,
        street: street.value,
        neighborhood: neighborhood.value,
        city: city.value,
        state: state.value,
      }
      const userData = await authStore.register(newUserData)
      globalStore.setAuthenticatedUserData(userData)
      alert('Cadastro realizado com sucesso. Verifique seu e-mail para a senha.')
      router.push('/reservas')
    }
  } catch (error) {
    console.error('Erro no processo de autenticação:', error)
    alert('Erro no processo de autenticação. Verifique os dados e tente novamente.')
  }
})
</script>

<template>
  <Tabs default-value="login" class="w-[400px]">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="register" @click="isLogin = false">Cadastro</TabsTrigger>
      <TabsTrigger value="login" @click="isLogin = true">Login</TabsTrigger>
    </TabsList>

    <TabsContent value="register">
      <Card>
        <CardHeader>
          <CardTitle>Cadastro</CardTitle>
          <CardDescription>Crie sua conta para ter acesso a todos os recursos.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form class="flex flex-col gap-3" @submit="onSubmit">
            <FormField name="name">
              <FormItem>
                <FormLabel>Nome</FormLabel>
                <FormControl>
                  <Input v-model="name.value.value" type="text" placeholder="Nome completo" />
                </FormControl>
                <FormMessage>{{ name.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="email">
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input v-model="email.value.value" type="email" placeholder="Email" />
                </FormControl>
                <FormMessage>{{ email.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="cpf">
              <FormItem>
                <FormLabel>CPF</FormLabel>
                <FormControl>
                  <Input v-model="cpf.value.value" type="text" placeholder="CPF" />
                </FormControl>
                <FormMessage>{{ cpf.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="cep">
              <FormItem>
                <FormLabel>CEP</FormLabel>
                <FormControl>
                  <Input v-model="cep.value.value" type="text" placeholder="CEP" />
                </FormControl>
                <FormMessage>{{ cep.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="street">
              <FormItem>
                <FormLabel>Rua</FormLabel>
                <FormControl>
                  <Input v-model="street" type="text" disabled />
                </FormControl>
              </FormItem>
            </FormField>

            <FormField name="neighborhood">
              <FormItem>
                <FormLabel>Bairro</FormLabel>
                <FormControl>
                  <Input v-model="neighborhood" type="text" disabled />
                </FormControl>
              </FormItem>
            </FormField>

            <FormField name="city">
              <FormItem>
                <FormLabel>Cidade</FormLabel>
                <FormControl>
                  <Input v-model="city" type="text" disabled />
                </FormControl>
              </FormItem>
            </FormField>

            <FormField name="state">
              <FormItem>
                <FormLabel>Estado</FormLabel>
                <FormControl>
                  <Input v-model="state" type="text" disabled />
                </FormControl>
              </FormItem>
            </FormField>

            <p class="text-sm text-muted-foreground mt-2">
              A senha será enviada para o e-mail após o cadastro.
            </p>

            <CardFooter class="p-0 pt-4">
              <Button type="submit" class="w-full">Cadastrar</Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>

    <TabsContent value="login">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
          <CardDescription>
            Entre com sua conta para ter acesso a todos os recursos.
          </CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form class="flex flex-col gap-3" @submit="onSubmit">
            <FormField name="email">
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input v-model="email.value.value" type="email" placeholder="Email" />
                </FormControl>
                <FormMessage>{{ email.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField name="password">
              <FormItem>
                <FormLabel>Senha</FormLabel>
                <FormControl>
                  <Input
                    v-model="password.value.value"
                    type="password"
                    placeholder="Senha (4 dígitos)"
                  />
                </FormControl>
                <FormMessage>{{ password.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <CardFooter class="p-0 pt-4">
              <Button type="submit" class="w-full">Login</Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>
  </Tabs>
</template>
