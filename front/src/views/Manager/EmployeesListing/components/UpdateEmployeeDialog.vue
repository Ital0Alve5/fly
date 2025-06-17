<script setup lang="ts">
import { ref, watch } from 'vue'
import { useToast } from '@/components/ui/toast'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Separator } from '@/components/ui/separator'
import { Button } from '@/components/ui/button'
import type { Employee } from '@/types/Auth/AuthenticatedUserData'

const { toast } = useToast()

const formSchema = toTypedSchema(
  z.object({
    nome: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string().email('E-mail inválido'),
    telefone: z.string().min(14, 'Telefone inválido').max(15, 'Telefone inválido'),
    cpf: z.string().min(11, 'CPF inválido'),
    senha: z.string().regex(/^[a-zA-Z0-9]{4}$/, 'Senha deve conter exatamente 4 caracteres').optional().or(z.literal(''))
  }),
)

const { handleSubmit, resetForm, setFieldValue } = useForm({
  validationSchema: formSchema,
})

const props = defineProps<{
  open: boolean
  employee: Employee | null
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (
    e: 'updated',
    data: {
      codigo: number
      nome: string
      email: string
      telefone: string
      cpf: string
      senha?: string
    },
  ): void
}>()


const openDialog = ref(props.open)
const isSubmitting = ref(false)

watch(
  () => props.open,
  (newVal) => {
    openDialog.value = newVal
    if (newVal && props.employee) {
      setFieldValue('nome', props.employee.nome)
      setFieldValue('email', props.employee.email)
      setFieldValue('telefone', props.employee.telefone)
      setFieldValue('cpf', props.employee.cpf)
      setFieldValue('senha', undefined)
    } else {
      resetForm()
    }
  },
)

watch(openDialog, (newVal) => {
  emit('update:open', newVal)
})

const onSubmit = handleSubmit(async (values) => {
  if (!props.employee) return

  isSubmitting.value = true
  try {
    emit('updated', {
      codigo: props.employee.codigo,
      nome: values.nome,
      email: values.email,
      telefone: values.telefone,
      cpf: values.cpf,
      senha: values.senha,
    })
    toast({
      title: 'Funcionário atualizado',
      description: 'Os dados foram enviados para atualização.',
      variant: 'default',
      duration: 800,
    })
    openDialog.value = false
  } finally {
    isSubmitting.value = false
  }
})

</script>

<template>
  <Dialog v-model:open="openDialog">
    <DialogContent class="max-w-md w-full px-6 py-8">
      <DialogHeader>
        <DialogTitle>Editar Funcionário</DialogTitle>
        <p class="text-sm text-muted-foreground">
          Atualize os dados cadastrais do funcionário.
        </p>
      </DialogHeader>

      <form @submit.prevent="onSubmit" class="flex flex-col gap-6 mt-4">
        <FormField v-slot="{ componentField }" name="nome">
          <FormItem>
            <FormLabel>Nome <span class="text-destructive">*</span></FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" class="focus-visible:ring-2 focus-visible:ring-primary" />
            </FormControl>
            <FormMessage class="text-xs text-destructive mt-1" />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="email">
          <FormItem>
            <FormLabel>E-mail <span class="text-destructive">*</span></FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="email" class="focus-visible:ring-2 focus-visible:ring-primary" />
            </FormControl>
            <FormMessage class="text-xs text-destructive mt-1" />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="cpf">
          <FormItem>
            <FormLabel>CPF <span class="text-destructive">*</span></FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'###.###.###-##'" disabled class="opacity-70 cursor-not-allowed" />
            </FormControl>
            <FormMessage class="text-xs text-destructive mt-1" />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="telefone">
          <FormItem>
            <FormLabel>Telefone <span class="text-destructive">*</span></FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="text" v-mask="'(##) #####-####'" class="focus-visible:ring-2 focus-visible:ring-primary" />
            </FormControl>
            <FormMessage class="text-xs text-destructive mt-1" />
          </FormItem>
        </FormField>

        <FormField v-slot="{ componentField }" name="senha">
          <FormItem>
            <FormLabel>Senha <span class="text-muted-foreground text-xs">(4 caracteres)</span></FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="password" maxlength="4" inputmode="numeric" class="focus-visible:ring-2 focus-visible:ring-primary" />
            </FormControl>
            <FormMessage class="text-xs text-destructive mt-1" />
          </FormItem>
        </FormField>

        <Separator class="my-4" />

        <div class="flex justify-end gap-2">
          <Button variant="outline" type="button" @click="openDialog = false">Cancelar</Button>
          <Button type="submit" :disabled="isSubmitting">
            <template v-if="isSubmitting">Salvando...</template>
            <template v-else>Salvar</template>
          </Button>
        </div>
      </form>
    </DialogContent>
  </Dialog>
</template>
