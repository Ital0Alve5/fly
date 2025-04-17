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
import { useAuthStore } from '@/stores/auth'
import type { Employee } from '@/types/Auth/AuthenticatedUserData'
import { isValidCPF } from '@/lib/utils'

const { toast } = useToast()
const authStore = useAuthStore()

const formSchema = toTypedSchema(
  z.object({
    name: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string().email('E-mail inválido'),
    phone: z.string().min(14, 'Telefone inválido').max(15, 'Telefone inválido'),
    cpf: z.string().refine((cpf) => isValidCPF(cpf), 'CPF inválido'),
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
    await authStore.registerEmployee(createEmployeeObject(values))

    toast({
      title: 'Funcionário adicionado',
      description: `Acesse sua senha no email ${values.email}.`,
      variant: 'default',
      duration: 500,
    })

    openDialog.value = false
  } catch (error: unknown) {
    const errorMessage = error instanceof Error ? error.message : 'Erro desconhecido'
    toast({
      title: 'Erro ao atualizar',
      description: 'Ocorreu um erro ao tentar adicionar o funcionário. ' + errorMessage,
      variant: 'destructive',
      duration: 500,
    })
  }
})

const createEmployeeObject = (values: {
  name?: string
  email?: string
  phone?: string
  cpf?: string
}): Employee => {
  return {
    codigo: Date.now(),
    nome: values.name ?? '',
    email: values.email ?? '',
    telefone: values.phone ?? '',
    cpf: values.cpf ?? '',
  }
}

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
        <FormField v-slot="{ componentField }" name="name">
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

        <FormField v-slot="{ componentField }" name="phone">
          <FormItem>
            <FormLabel>Telefone</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'(##) #####-####'" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <div class="flex justify-end gap-2 mt-4">
          <Button variant="destructive" type="button" @click="cancel"> Cancelar </Button>
          <Button type="submit"> Salvar </Button>
        </div>
      </form>
    </DialogContent>
  </Dialog>
</template>
