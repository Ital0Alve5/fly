<script setup lang="ts">
import { computed } from 'vue'
import { Label } from '@/components/ui/label'
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput,
} from '@/components/ui/number-field'
import { Separator } from '@/components/ui/separator'
import { useAuthStore } from '@/stores/auth'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { useMilesStore } from '@/stores/miles'

const authStore = useAuthStore()
const milesStore = useMilesStore()

const user = computed(() => ({
  firstName: authStore.user?.name.split(' ')[0],
}))
const currentCheckoutMiles = computed({
  get: () => milesStore.currentCheckoutMiles,
  set: (value: number) => milesStore.setCurrentCheckoutMiles(value),
})
const totalMiles = computed({
  get: () => milesStore.totalMiles,
  set: (value: number) => milesStore.setTotalMiles(value),
})
const pricePerMile = computed(() => milesStore.pricePerMile)
const totalPrice = computed(() => milesStore.totalPrice)
</script>

<template>
  <div class="h-full w-full flex flex-col border-0 space-y-6">
    <div class="p-6">
      <h2 class="text-lg font-semibold text-center">Informações de {{ user.firstName }}</h2>
    </div>

    <div class="px-6 pb-6">
      <ul class="space-y-4">
        <li>
          <span class="font-medium w-32 shrink-0">Minhas milhas:</span>
          <span class="pl-2">{{ totalMiles }}</span>
        </li>
      </ul>
    </div>

    <Separator />

    <div class="p-6 pt-12">
      <h2 class="text-lg font-semibold text-center">Selecione a quantidade de milhas</h2>
    </div>

    <div class="px-6 pb-6">
      <NumberField id="miles" v-model="currentCheckoutMiles" :min="0">
        <Label>Milhas</Label>
        <NumberFieldContent>
          <NumberFieldDecrement />
          <NumberFieldInput/>
          <NumberFieldIncrement />
        </NumberFieldContent>
      </NumberField>
    </div>

    <div class="px-6 pb-6">
      <Card>
        <CardHeader>
          <CardTitle class="text-center"> Resumo da compra </CardTitle>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="flex justify-between">
            <span class="font-medium">Milhas:</span>
            <span>{{ currentCheckoutMiles }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Preço por milha:</span>
            <span>{{ pricePerMile.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }}</span>
          </div>
          <Separator />
          <div class="flex justify-between font-bold">
            <span>Total:</span>
            <span>{{ totalPrice.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }}</span>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>
