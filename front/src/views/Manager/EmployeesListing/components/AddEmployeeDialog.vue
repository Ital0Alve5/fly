<script setup lang="ts">
import { ref, watch } from 'vue'
import { useToast } from '@/components/ui/toast'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'

import { isValidCPF } from '@/lib/utils'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import addNewEmployee from '../services/addNewEmployee'
import { AxiosError } from 'axios'

const { toast } = useToast()
const isLoading = ref(false)

const formSchema = toTypedSchema(
  z.object({
    nome: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string().email('E-mail inválido'),
    telefone: z.string().min(14, 'Telefone inválido').max(15, 'Telefone inválido'),
    cpf: z.string().refine((cpf) => isValidCPF(cpf), 'CPF inválido'),
    senha: z.string().regex(/^\d{4}$/, 'Senha deve conter exatamente 4 dígitos numéricos')
  }),
)

const { handleSubmit, resetForm } = useForm({
  validationSchema: formSchema,
})

const props = defineProps<{
  open: boolean
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'added'): void
}>()

const openDialog = ref(props.open)

watch(
  () => props.open,
  (newVal) => {
    openDialog.value = newVal
  },
)

watch(openDialog, (newVal) => {
  emit('update:open', newVal)
})

const onSubmit = handleSubmit(async (values) => {
  try {
    isLoading.value = true
    await addNewEmployee(values)

    isLoading.value = false
    emit('added')

    toast({
      title: 'Funcionário adicionado',
      description: `Acesse sua senha no email ${values.email}.`,
      variant: 'default',
      duration: 500,
    })
  } catch (error) {
    isLoading.value = false

    if (error instanceof AxiosError) {
      if (error.response?.status === 409) {
        toast({
          title: 'Erro ao cadastrar funcionário',
          description: 'CPF ou E-mail já cadastrado.',
          variant: 'destructive',
        })
      } else if (error.response) {
        toast({
          title: `Erro ${error.response.status}`,
          description: error.response.statusText,
          variant: 'destructive',
        })
      } else {
        toast({
          title: 'Erro de conexão',
          description: 'Não foi possível conectar ao servidor.',
          variant: 'destructive',
        })
      }
    } else {
      toast({
        title: 'Erro inesperado',
        description: 'Tente novamente mais tarde.',
        variant: 'destructive',
      })
    }

    console.error('Erro ao adicionar funcionário:', error)
  } finally {
    openDialog.value = false
  }
})

const cancel = () => {
  openDialog.value = false
  resetForm()
}
</script>

<template>
  <Dialog v-model:open="openDialog">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Adicionar Funcionário</DialogTitle>
      </DialogHeader>

      <form @submit.prevent="onSubmit" class="flex flex-col gap-4">
        <FormField v-slot="{ componentField }" name="nome">
          <FormItem>
            <FormLabel>Nome</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="email">
          <FormItem>
            <FormLabel>E-mail</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="email" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="cpf">
          <FormItem>
            <FormLabel>CPF</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'###.###.###-##'" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="telefone">
          <FormItem>
            <FormLabel>Telefone</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'(##) #####-####'" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="senha">
          <FormItem>
            <FormLabel>Senha</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="password" inputmode="numeric" maxlength="6" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <div class="flex justify-end gap-2 mt-4">
          <Button variant="destructive" type="button" @click="cancel"> Cancelar </Button>
          <Button type="submit" :disabled="isLoading"
            ><LoadingSpinner v-if="isLoading" />
            <p v-else>Salvar</p>
          </Button>
        </div>
      </form>
    </DialogContent>
  </Dialog>
</template>
