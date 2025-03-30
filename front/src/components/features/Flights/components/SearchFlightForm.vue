<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue'
import { FormField } from '@/components/ui/form'
import FormControl from '@/components/ui/form/FormControl.vue'
import FormItem from '@/components/ui/form/FormItem.vue'
import Input from '@/components/ui/input/Input.vue'
import { useSearchFlightForm } from '../composables/useSearchFlightForm'

defineProps<{ loading: boolean }>()
const emit = defineEmits(['onSearch'])

const { handleSubmit } = useSearchFlightForm()

const onSubmit = handleSubmit((values) => {
  emit('onSearch', values.originAirpoirt ?? '', values.destinationAirpoirt ?? '')
})
</script>

<template>
  <form @submit.prevent="onSubmit" class="flex items-center mt-8">
    <FormField name="originAirpoirt" v-slot="{ componentField }">
      <FormItem class="w-full">
        <FormControl>
          <Input
            class="rounded-r-none"
            v-bind="componentField"
            type="text"
            placeholder="Aeroporto de origem"
          />
        </FormControl>
      </FormItem>
    </FormField>
    <FormField name="destinationAirpoirt" v-slot="{ componentField }">
      <FormItem class="w-full">
        <FormControl>
          <Input
            class="rounded-none"
            v-bind="componentField"
            type="text"
            placeholder="Aeroporto de destino"
          />
        </FormControl>
      </FormItem>
    </FormField>
    <Button :disabled="loading" type="submit" class="rounded-l-none">Buscar</Button>
  </form>
</template>
