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
import { useToast } from '@/components/ui/toast'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { useMilesStore } from '@/stores/miles'
import { addMilesToClient } from '@/clientService/ClientService'
import router from '@/router'

const milesStore = useMilesStore()
const { toast } = useToast()

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

const onSubmit = async () => {
  try {
    await addMilesToClient(currentCheckoutMiles.value);
    await milesStore.refreshMiles()
    milesStore.setCurrentCheckoutMiles(10)

    toast({
      title: 'Pagamento efetuado com sucesso',
      description: 'Suas milhas foram compradas com sucesso!',
      variant: 'default',
      duration: 2000,
    })

    router.push('/reservas')
  } catch {
    toast({
      title: 'Erro ao comprar milhas',
      description: 'Houve um erro ao registrar a sua compra de milhas!',
      variant: 'destructive',
      duration: 2000,
    })
  }
}
</script>

<template>
  <div class="h-full w-full flex flex-col border-0 space-y-6">
    <div class="space-y-4">
      <span class="font-medium w-32 shrink-0">Minhas milhas:</span>
      <span class="pl-2">{{ totalMiles }}</span>
    </div>

    <Separator />

    <h2 class="text-lg font-semibold text-center">Selecione a quantidade de milhas</h2>

    <div class="px-6 pb-6">
      <NumberField id="miles" v-model="currentCheckoutMiles" :min="0" :max="10000000">
        <Label>Milhas</Label>
        <NumberFieldContent>
          <NumberFieldDecrement />
          <NumberFieldInput />
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
            <span>{{
              pricePerMile.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            }}</span>
          </div>
          <Separator />
          <div class="flex justify-between font-bold">
            <span>Total:</span>
            <span>{{
              totalPrice.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            }}</span>
          </div>
        </CardContent>
      </Card>
    </div>
    <Button @click="onSubmit" class="w-full mt-6">
      Pagar
      {{ totalPrice.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }}</Button
    >
  </div>
</template>
