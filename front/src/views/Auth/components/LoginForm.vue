<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useGlobalStore } from '@/stores/global'
import login from '../services/login'

import { useLoginForm } from '../composables/useLoginForm'
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'

const router = useRouter()
const authStore = useAuthStore()
const globalStore = useGlobalStore()

const { handleSubmit, email, password } = useLoginForm()

const onSubmit = handleSubmit(async (values) => {
  try {
    const userData = await login({ login: values.email, senha: values.password })

    if (userData) {
      authStore.login(userData.data)

      router.push(userData.data.tipo === 'FUNCIONARIO' ? '/adm/voos' : '/reservas')
    } else {
      globalStore.setNotification({
        title: 'Erro no login!',
        description: 'Verifique seu e-mail e senha.',
        variant: 'destructive',
      })
    }
  } catch (error) {
    console.log(error)
    globalStore.setNotification({
      title: 'Erro no login!',
      description: 'Verifique seu e-mail e senha.',
      variant: 'destructive',
    })
  }
})
</script>

<template>
  <Card>
    <CardHeader>
      <CardTitle>Login</CardTitle>
      <CardDescription>Entre com sua conta para acessar os recursos.</CardDescription>
    </CardHeader>
    <CardContent>
      <form @submit.prevent="onSubmit" class="flex flex-col gap-3">
        <FormField name="email" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>E-mail</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="email" placeholder="Email" />
            </FormControl>
            <FormMessage>{{ email.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="password" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Senha</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="password" placeholder="Senha (6 dígitos)" />
            </FormControl>
            <FormMessage>{{ password.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <Button type="submit" class="mt-4 w-full">Login</Button>
      </form>
    </CardContent>
  </Card>
</template>
