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
import { Separator } from '@/components/ui/separator'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import { isValidCPF } from '@/lib/utils'
import addNewEmployee from '../services/addNewEmployee'
import { AxiosError } from 'axios'

const { toast } = useToast()
const isLoading = ref(false)

const formSchema = toTypedSchema(
  z.object({
    nome: z.string({ required_error: 'Nome é obrigatório' }).min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string({ required_error: 'E-mail é obrigatório' }).email('E-mail inválido'),
    telefone: z.string({ required_error: 'Telefone é obrigatório' }).min(14, 'Telefone inválido').max(15, 'Telefone inválido'),
    cpf: z.string({ required_error: 'CPF é obrigatório' }).refine((cpf) => isValidCPF(cpf), 'CPF inválido'),
    senha: z.string().regex(/^[a-zA-Z0-9]{4}$/, 'Senha deve conter exatamente 4 caracteres'),
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

watch(() => props.open, (newVal) => {
  openDialog.value = newVal
})

watch(openDialog, (newVal) => {
  emit('update:open', newVal)
})

const onSubmit = handleSubmit(async (values) => {
  try {
    isLoading.value = true
    await addNewEmployee(values)
    emit('added')

    toast({
      title: 'Funcionário adicionado',
      description: `A senha foi enviada para o e-mail: ${values.email}.`,
      variant: 'default',
      duration: 800,
    })
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.response?.status === 409) {
        toast({
          title: 'Erro ao cadastrar',
          description: 'CPF ou e-mail já estão cadastrados.',
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
    isLoading.value = false
    openDialog.value = false
    resetForm()
  }
})

const cancel = () => {
  openDialog.value = false
  resetForm()
}
</script>

<template>
  <Dialog v-model:open="openDialog">
    <DialogContent class="max-w-md w-full px-6 py-8">
      <DialogHeader>
        <DialogTitle>Adicionar Funcionário</DialogTitle>
        <p class="text-sm text-muted-foreground">
          Preencha os campos abaixo para cadastrar um novo funcionário.
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
              <Input v-bind="componentField" type="text" v-mask="'###.###.###-##'" class="focus-visible:ring-2 focus-visible:ring-primary" />
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
              <Input v-bind="componentField" type="password" inputmode="numeric" maxlength="4" class="focus-visible:ring-2 focus-visible:ring-primary" />
            </FormControl>
            <FormMessage class="text-xs text-destructive mt-1" />
          </FormItem>
        </FormField>

        <Separator class="my-4" />

        <div class="flex justify-end gap-2">
          <Button variant="outline" type="button" @click="cancel">Cancelar</Button>
          <Button type="submit" :disabled="isLoading">
            <template v-if="isLoading">
              <LoadingSpinner />
              Salvando...
            </template>
            <template v-else>Salvar</template>
          </Button>
        </div>
      </form>
    </DialogContent>
  </Dialog>
</template>
