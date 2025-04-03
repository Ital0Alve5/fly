<script setup lang="ts">
import { computed } from 'vue'
import { Button } from '@/components/ui/button'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Separator } from '@/components/ui/separator'
import { useToast } from '@/components/ui/toast'
import { useCheckoutForm } from '../composables/useCheckoutForm'
import { useMilesStore } from '@/stores/miles'
import { registerExtract, type ExtractItem } from '@/mock/extract'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'
import { getTodayDate } from '@/utils/date/getTodayDate'

const authStore = useAuthStore()

const { toast } = useToast()
const milesStore = useMilesStore()

const { handleSubmit, cardNumber, cardName, expiryDate, cvv, resetForm } = useCheckoutForm()
const currentCheckoutMiles = computed({
  get: () => milesStore.currentCheckoutMiles,
  set: (value) => milesStore.setCurrentCheckoutMiles(value)
})
const totalMiles = computed({
  get: () => milesStore.totalMiles,
  set: (value) => milesStore.setTotalMiles(value)
})
const totalPrice = computed(() => milesStore.totalPrice)

const formatCardNumber = (e: Event) => {
  const target = e.target as HTMLInputElement
  let value = target.value.replace(/\s+/g, '')
  if (value.length > 0) {
    value = value.match(new RegExp('.{1,4}', 'g'))!.join(' ')
  }
  cardNumber.value.value = value
}

const formatExpiryDate = (e: Event) => {
  const target = e.target as HTMLInputElement
  let value = target.value.replace(/\D/g, '')

  if (value.length <= 2 && target.value.includes('/')) {
    value = value.substring(0, 2)
    expiryDate.value.value = value
    return
  }

  if (value.length >= 2) {
    value = value.substring(0, 2) + '/' + value.substring(2, 4)
  }

  expiryDate.value.value = value
}

const onSubmit = handleSubmit(async (values) => {

  const newExtract: ExtractItem = {
    userId: authStore.user?.userId || 0,
    date: getTodayDate(),
    reservationCode: null,
    value: totalPrice.value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    miles: currentCheckoutMiles.value,
    description: 'COMPRA DE MILHAS',
    type: 'ENTRADA',
  }

  registerExtract(newExtract)
  milesStore.setTotalMiles(totalMiles.value + currentCheckoutMiles.value)
  milesStore.setCurrentCheckoutMiles(10)

  console.log('Informações de pagamento:', values)

  toast({
    title: 'Pagamento efetuado com sucesso',
    description: 'Suas milhas foram compradas com sucesso!',
    variant: 'default',
    duration: 2000,
  })

  resetForm()
  router.push('/reservas')
})
</script>

<template>
  <div class="h-full w-full flex flex-col border-0 space-y-6">
    <div class="p-6">
      <h2 class="text-lg font-semibold text-center">Finalização de pagamento</h2>
    </div>

    <div class="px-6 pb-6">
      <Separator />
    </div>

    <div class="px-6 pb-6">
      <h3 class="text-lg font-semibold text-center pb-4">Detalhes do pagamento</h3>
      <form @submit.prevent="onSubmit">
        <div class="space-y-4">
          <FormField v-slot="{ componentField }" name="cardNumber">
            <FormItem>
              <FormLabel>Número do cartão</FormLabel>
              <FormControl>
                <Input
                  type="text"
                  placeholder="1234 5678 9012 3456"
                  v-bind="componentField"
                  @input="formatCardNumber"
                  maxlength="19"
                />
              </FormControl>
              <FormMessage>{{ cardNumber.errorMessage.value }}</FormMessage>
            </FormItem>
          </FormField>

          <FormField v-slot="{ componentField }" name="cardName">
            <FormItem>
              <FormLabel>Nome do titular</FormLabel>
              <FormControl>
                <Input type="text" placeholder="Maria João" v-bind="componentField" />
              </FormControl>
              <FormMessage>{{ cardName.errorMessage.value }}</FormMessage>
            </FormItem>
          </FormField>

          <div class="grid grid-cols-2 gap-4">
            <FormField v-slot="{ componentField }" name="expiryDate">
              <FormItem>
                <FormLabel>Data de validade</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="MM/YY"
                    v-bind="componentField"
                    @input="formatExpiryDate"
                    maxlength="5"
                  />
                </FormControl>
                <FormMessage>{{ expiryDate.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>

            <FormField v-slot="{ componentField }" name="cvv">
              <FormItem>
                <FormLabel>CVV</FormLabel>
                <FormControl>
                  <Input type="text" placeholder="123" v-bind="componentField" maxlength="4" />
                </FormControl>
                <FormMessage>{{ cvv.errorMessage.value }}</FormMessage>
              </FormItem>
            </FormField>
          </div>

          <Button type="submit" class="w-full mt-6"> Pagar {{ totalPrice.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }}</Button>
        </div>
      </form>
    </div>
  </div>
</template>
