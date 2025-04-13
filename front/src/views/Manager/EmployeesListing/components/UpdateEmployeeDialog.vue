<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { useToast } from '@/components/ui/toast'
import { useForm } from 'vee-validate'
import { toTypedSchema } from '@vee-validate/zod'
import * as z from 'zod'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import employeeService from '@/mock/employees'
import type { Employee } from '@/types/Auth/AuthenticatedUserData'

const { toast } = useToast()

const formSchema = toTypedSchema(
  z.object({
    name: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
    email: z.string().email('E-mail inválido'),
    phone: z.string().min(14, 'Telefone inválido').max(15, 'Telefone inválido'),
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
  (e: 'refresh'): void
}>()

const employees = computed(() =>
  employeeService.registeredEmployees.value.slice().sort((a, b) => a.nome.localeCompare(b.nome)),
)

const openDialog = ref(props.open)

watch(
  () => props.open,
  (newVal) => {
    openDialog.value = newVal
    if (newVal && props.employee?.codigo) {
      const employee = employees.value.find((e) => e.codigo === props.employee?.codigo)
      if (employee) {
        setFieldValue('name', employee.nome)
        setFieldValue('email', employee.email)
        setFieldValue('phone', employee.telefone)
      }
    } else {
      resetForm()
    }
  },
)

watch(openDialog, (newVal) => {
  emit('update:open', newVal)
})

const onSubmit = handleSubmit(async (values) => {
  if (!props.employee?.codigo) return

  try {
    const index = employeeService.registeredEmployees.value.findIndex(
      (e) => e.codigo === props.employee?.codigo,
    )

    if (index !== -1) {
      employeeService.registeredEmployees.value[index] = {
        ...employeeService.registeredEmployees.value[index],
        nome: values.name,
        email: values.email,
        telefone: values.phone,
      }

      toast({
        title: 'Funcionário atualizado',
        description: 'Os dados do funcionário foram atualizados com sucesso.',
        variant: 'default',
        duration: 400,
      })

      emit('refresh')
      openDialog.value = false
    }
  } catch {
    toast({
      title: 'Erro ao atualizar',
      description: 'Ocorreu um erro ao tentar atualizar os dados do funcionário.',
      variant: 'destructive',
      duration: 400,
    })
  }
})

const cancelEdit = () => {
  openDialog.value = false
}
</script>

<template>
  <Dialog v-model:open="openDialog">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Editar Funcionário</DialogTitle>
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
          <Button variant="destructive" type="button" @click="cancelEdit"> Cancelar </Button>
          <Button type="submit"> Salvar </Button>
        </div>
      </form>
    </DialogContent>
  </Dialog>
</template>
