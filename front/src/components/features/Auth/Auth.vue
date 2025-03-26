<script setup lang="ts">
import { useForm, useField } from 'vee-validate'
import { useAuthStore } from '@/stores/authStore'
import { useGlobalStore } from '@/stores/global'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
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

const onSubmit = handleSubmit(async (values) => {
  try {
    if (isLogin.value) {
      const userData = await authStore.login(values.email, values.password)
      if (userData) {
        globalStore.setAuthenticatedUserData(userData)
        router.push('/home')
      } else {
        alert('Login inválido')
      }
    } else {
      const newUserData = {
        name: values.name,
        email: values.email,
        cpf: values.cpf,
        cep: values.cep,
      }
      const userData = await authStore.register(newUserData, values.password)
      globalStore.setAuthenticatedUserData(userData)
      router.push('/home')
    }
  } catch {
    console.error('erro')
  }
})
</script>

<template>
  <Tabs default-value="login" class="w-[400px]">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="register" @click="isLogin = false">Cadastro</TabsTrigger>
      <TabsTrigger value="login" @click="isLogin = true">Login</TabsTrigger>
    </TabsList>

    <!-- Aba Cadastro -->
    <TabsContent value="register">
      <Card>
        <CardHeader>
          <CardTitle>Cadastro</CardTitle>
          <CardDescription>Crie sua conta para ter acesso a todos os recursos.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form @submit="onSubmit">
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
              <Button type="submit" class="w-full">Cadastrar</Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>

    <!-- Aba Login -->
    <TabsContent value="login">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
          <CardDescription
            >Entre com sua conta para ter acesso a todos os recursos.</CardDescription
          >
        </CardHeader>
        <CardContent class="space-y-2">
          <form @submit="onSubmit">
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
                  <Input v-model="password.value.value" type="password" placeholder="Senha" />
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
