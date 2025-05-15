<script setup lang="ts">
import { ref } from 'vue'
import { useToast } from '@/components/ui/toast'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'

import {
  FormField, FormItem, FormLabel, FormControl, FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import {
  Dialog, DialogContent, DialogHeader, DialogTitle,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'

import { isValidCPF } from '@/lib/utils'

const { toast } = useToast()

const formSchema = toTypedSchema(
  z.object({
    nome: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string().email('E-mail inválido'),
    telefone: z.string().regex(/^\(\d{2}\) \d{5}-\d{4}$/, 'Telefone inválido'),
    cpf: z.string().refine((cpf) => isValidCPF(cpf), 'CPF inválido'),
    senha: z.string().regex(/^\d{1,6}$/, 'Senha deve conter apenas números e até 6 dígitos'),
  })
)

type FormValues = z.infer<typeof formSchema>

const model = defineModel<boolean>('open')
const emit = defineEmits<{
  (e: 'added', payload: FormValues): void
}>()

const {
  handleSubmit,
  resetForm,
  isSubmitting,
} = useForm({
  validationSchema: formSchema,
})

const onSubmit = handleSubmit((values) => {
  emit('added', values)

  toast({
    title: 'Funcionário adicionado',
    description: `Acesse sua senha no email ${values.email}.`,
    variant: 'default',
    duration: 500,
  })

  resetForm()
  model.value = false
})

const cancel = () => {
  model.value = false
  resetForm()
}
</script>

<template>
  <Dialog v-model:open="model">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Adicionar Funcionário</DialogTitle>
      </DialogHeader>

      <form @submit.prevent="onSubmit" class="flex flex-col gap-4">
        <FormField v-slot="{ componentField }" name="nome">
          <FormItem>
            <FormLabel>Nome</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" autocomplete="name" autofocus />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="email">
          <FormItem>
            <FormLabel>E-mail</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="email" autocomplete="email" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="cpf">
          <FormItem>
            <FormLabel>CPF</FormLabel>
            <FormControl>
              <Input
                v-bind="componentField"
                type="text"
                v-mask="'###.###.###-##'"
                autocomplete="off"
                aria-label="CPF"
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="telefone">
          <FormItem>
            <FormLabel>Telefone</FormLabel>
            <FormControl>
              <Input
                v-bind="componentField"
                type="text"
                v-mask="'(##) #####-####'"
                autocomplete="tel"
                aria-label="Telefone"
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="senha">
          <FormItem>
            <FormLabel>Senha</FormLabel>
            <FormControl>
              <Input
                v-bind="componentField"
                type="text"
                inputmode="numeric"
                maxlength="6"
                pattern="\d{1,6}"
                autocomplete="new-password"
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <div class="flex justify-end gap-2 mt-4">
          <Button variant="destructive" type="button" @click="cancel">Cancelar</Button>
          <Button type="submit" :disabled="isSubmitting">
            <span v-if="isSubmitting">Salvando...</span>
            <span v-else>Salvar</span>
          </Button>
        </div>
      </form>
    </DialogContent>
  </Dialog>
</template>
