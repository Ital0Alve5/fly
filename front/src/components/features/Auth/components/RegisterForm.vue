<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useRegisterForm } from '../composables/useRegisterForm'
import { useAddressByCep } from '../composables/useAddressByCep'
import { useAuthStore } from '@/stores/auth'
import { useGlobalStore } from '@/stores/global'

import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from '@/components/ui/card'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'

const router = useRouter()
const authStore = useAuthStore()
const globalStore = useGlobalStore()

const { handleSubmit, name, registerEmail, cpf, cep } = useRegisterForm()

const { street, neighborhood, city, state } = useAddressByCep(cep.value)

const onSubmit = handleSubmit(async (values) => {
  const newUserData = {
    name: values.name,
    email: values.registerEmail,
    cpf: values.cpf,
    cep: values.cep,
    street: street.value,
    neighborhood: neighborhood.value,
    city: city.value,
    state: state.value,
  }

  await authStore.register(newUserData)

  globalStore.setNotification({
    title: 'Cadastro realizado!',
    description: 'Verifique seu e-mail para a senha',
    variant: 'default',
  })

  router.push('/reservas')
})
</script>

<template>
  <Card>
    <CardHeader>
      <CardTitle>Cadastro</CardTitle>
      <CardDescription>Crie sua conta para acessar os recursos.</CardDescription>
    </CardHeader>

    <CardContent>
      <form @submit.prevent="onSubmit" class="flex flex-col gap-1">
        <FormField name="name" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Nome</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" />
            </FormControl>
            <FormMessage>{{ name.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="registerEmail" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>E-mail</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="email" />
            </FormControl>
            <FormMessage>{{ registerEmail.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="cpf" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>CPF</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'###.###.###-##'" />
            </FormControl>
            <FormMessage>{{ cpf.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="cep" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>CEP</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'#####-###'" />
            </FormControl>
            <FormMessage>{{ cep.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormItem>
          <FormLabel>Rua</FormLabel>
          <FormControl>
            <Input v-model="street" type="text" disabled />
          </FormControl>
        </FormItem>

        <FormItem>
          <FormLabel>Bairro</FormLabel>
          <FormControl>
            <Input v-model="neighborhood" type="text" disabled />
          </FormControl>
        </FormItem>

        <FormItem>
          <FormLabel>Cidade</FormLabel>
          <FormControl>
            <Input v-model="city" type="text" disabled />
          </FormControl>
        </FormItem>

        <FormItem>
          <FormLabel>Estado</FormLabel>
          <FormControl>
            <Input v-model="state" type="text" disabled />
          </FormControl>
        </FormItem>

        <p class="text-sm text-muted-foreground mt-2">
          A senha será enviada para o e-mail após o cadastro.
        </p>

        <CardFooter class="p-0 pt-4">
          <Button type="submit" class="w-full">Cadastrar</Button>
        </CardFooter>
      </form>
    </CardContent>
  </Card>
</template>
