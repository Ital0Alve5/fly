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
import type { Client } from '@/types/Auth/AuthenticatedUserData'

const router = useRouter()
const authStore = useAuthStore()
const globalStore = useGlobalStore()

const { handleSubmit, name, registerEmail, cpf, cep } = useRegisterForm()

const { street, neighborhood, city, state, number, complement } = useAddressByCep(cep.value)

const onSubmit = handleSubmit(async (values) => {
  const newUserData: Client = {
    codigo: Date.now(),
    nome: values.name,
    email: values.registerEmail,
    cpf: values.cpf,
    saldo_milhas: 0,
    endereco: {
      cep: values.cep,
      rua: street.value,
      bairro: neighborhood.value,
      cidade: city.value,
      uf: state.value,
      complemento: complement.value,
      numero: number.value,
    },
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

        <div class="flex justify-between">
          <FormItem>
            <FormLabel>Rua</FormLabel>
            <FormControl>
              <Input v-model="street" type="text" disabled />
            </FormControl>
          </FormItem>
          <FormItem class="max-w-20">
            <FormLabel>Número</FormLabel>
            <FormControl>
              <Input v-model="number" type="number" />
            </FormControl>
          </FormItem>
        </div>

        <FormItem>
          <FormLabel>Bairro</FormLabel>
          <FormControl>
            <Input v-model="neighborhood" type="text" disabled />
          </FormControl>
        </FormItem>
        <div class="flex justify-between">
          <FormItem>
            <FormLabel>Cidade</FormLabel>
            <FormControl>
              <Input v-model="city" type="text" disabled />
            </FormControl>
          </FormItem>

          <FormItem class="max-w-20">
            <FormLabel>Estado</FormLabel>
            <FormControl>
              <Input v-model="state" type="text" disabled />
            </FormControl>
          </FormItem>
        </div>

        <FormItem>
          <FormLabel>Complemento</FormLabel>
          <FormControl>
            <Input v-model="complement" type="text" />
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
