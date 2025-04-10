<script setup lang="ts">
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import DialogFooter from '@/components/ui/dialog/DialogFooter.vue'
import { FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import Select from '@/components/ui/select/Select.vue'
import SelectContent from '@/components/ui/select/SelectContent.vue'
import SelectGroup from '@/components/ui/select/SelectGroup.vue'
import SelectItem from '@/components/ui/select/SelectItem.vue'
import SelectTrigger from '@/components/ui/select/SelectTrigger.vue'
import SelectValue from '@/components/ui/select/SelectValue.vue'
import { loadAllAirpoirts } from '@/mock/airpoirts'
import { useGlobalStore } from '@/stores/global'
import type { Airport } from '@/types/Airpoirt'
import { computed, onMounted, ref } from 'vue'
import { useRegisterFlightForm } from '../composables/useRegisterFlightForm'
import { extractPriceValue } from '@/utils/currency/extractPriceValue'
import { currencyMask } from '@/utils/currency/inputMask'
import { registerFlight } from '@/mock/flight'

defineProps<{
  modelValue: boolean
}>()

const emit = defineEmits(['update:modelValue', 'handleFlightRegistered'])

const globalStore = useGlobalStore()
const { handleSubmit, code, originAirport, destinationAirport, seatsNumber, priceField, date } =
  useRegisterFlightForm()
const loading = ref(false)

const price = computed(() => extractPriceValue(priceField.value.value))
const totalMiles = computed(() => Math.ceil(price.value) / 5)

const airpoirtsList = ref<Airport[]>([])

async function handleLoadAirpoirts() {
  try {
    const aipoirts = await loadAllAirpoirts()

    airpoirtsList.value = aipoirts
  } catch (err) {
    console.log(err)

    globalStore.setNotification({
      title: 'Erro ao carregar os aeroportos!',
      description: 'Tente novamente mais tarde.',
      variant: 'destructive',
    })
  }
}

onMounted(() => {
  handleLoadAirpoirts()
})

const handleOpenChange = (open: boolean) => {
  emit('update:modelValue', open)
}

const onSubmit = handleSubmit(async (data) => {
  try {
    if (loading.value) {
      return
    }

    loading.value = true
    await registerFlight({
      ...data,
      price: price.value,
    })

    globalStore.setNotification({
      title: 'Voo cadastrado com sucesso',
      description: 'O voo foi cadastrado com sucesso.',
      variant: 'default',
    })

    emit('update:modelValue', false)
    emit('handleFlightRegistered')
  } catch (err) {
    globalStore.setNotification({
      title: 'Erro ao cadastrar voo!',
      description: err instanceof Error ? err.message : 'Tente novamente mais tarde',
      variant: 'destructive',
    })
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <Dialog :open="modelValue" @update:open="handleOpenChange">
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Cadastrar Voo</DialogTitle>
        <br />
        <DialogDescription>Insira as informações do voo a ser cadastrado. </DialogDescription>
      </DialogHeader>
      <form @submit.prevent="onSubmit" class="flex flex-col gap-3">
        <FormField name="code" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Código do Voo</FormLabel>
            <FormControl>
              <Input mask="TADS####" v-bind="componentField" type="text" placeholder="TADS0000" />
            </FormControl>
            <FormMessage>{{ code.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="date" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Data da viagem</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="datetime-local" />
            </FormControl>
            <FormMessage>{{ date.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="originAirport" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Aeroporto de origem</FormLabel>
            <FormControl>
              <Select v-bind="componentField">
                <SelectTrigger>
                  <SelectValue placeholder="Selecione um aeroporto de origem" />
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    <SelectItem
                      :key="aipoirt.code"
                      v-for="aipoirt in airpoirtsList"
                      :value="aipoirt.code"
                      >{{ aipoirt.code }} - {{ aipoirt.name }}
                    </SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </FormControl>
            <FormMessage>{{ originAirport.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="destinationAirport" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Aeroporto de destino</FormLabel>
            <FormControl>
              <Select v-bind="componentField">
                <SelectTrigger>
                  <SelectValue placeholder="Selecione um aeroporto de destino" />
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    <SelectItem
                      :key="aipoirt.code"
                      v-for="aipoirt in airpoirtsList"
                      :value="aipoirt.code"
                      >{{ aipoirt.code }} - {{ aipoirt.name }}
                    </SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </FormControl>
            <FormMessage>{{ destinationAirport.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="price" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Valor da passagem</FormLabel>
            <FormControl>
              <Input
                :mask="currencyMask"
                v-bind="componentField"
                type="string"
                placeholder="R$1000,00"
              />
            </FormControl>
            <span class="text-xs mt-4 block">Equivalente em milhas: {{ totalMiles }}</span>
            <FormMessage>{{ priceField.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <FormField name="seatsNumber" v-slot="{ componentField }">
          <FormItem>
            <FormLabel>Quantidade de poltronas</FormLabel>
            <FormControl>
              <Input v-bind="componentField" type="number" placeholder="Quantidade de poltronas" />
            </FormControl>
            <FormMessage>{{ seatsNumber.errorMessage.value }}</FormMessage>
          </FormItem>
        </FormField>

        <DialogFooter class="flex justify-end space-x-1 w-full mt-2">
          <Button :disabled="loading">Cadastrar</Button>
          <Button
            :disabled="loading"
            type="button"
            @click="$emit('update:modelValue', false)"
            variant="destructive"
            >Cancelar</Button
          >
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>
