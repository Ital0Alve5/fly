<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
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
import { useAuthStore } from '@/stores/auth'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import router from '@/router'
import { MilesService } from '@/services/milesService'

const authStore = useAuthStore()
const { toast } = useToast()

const currentCheckoutMiles = ref(10)
const totalMiles = ref(0)
const pricePerMile = 5
const isLoading = ref(false)

const totalPrice = computed(() => currentCheckoutMiles.value * pricePerMile)

onMounted(async () => {
  if (!authStore.user?.usuario.codigo) return
  
  try {
    const response = await MilesService.getSummary(authStore.user.usuario.codigo)
    if (!response.error) {
      totalMiles.value = response.data.saldo_milhas
    } else {
      toast({
        title: 'Erro',
        description: response.message,
        variant: 'destructive',
        duration: 2000,
      })
    }
  } catch (error) {
    const errorMessage = error instanceof Error 
      ? error.message 
      : 'Falha ao carregar saldo de milhas'
    toast({
      title: 'Erro',
      description: errorMessage,
      variant: 'destructive',
      duration: 2000,
    })
  }
})

const onSubmit = async () => {
  if (!authStore.user?.usuario.codigo) return
  
  isLoading.value = true
  
  try {
    const response = await MilesService.addMiles(
      authStore.user.usuario.codigo,
      currentCheckoutMiles.value
    )

    if (!response.error) {
      totalMiles.value = response.data.saldo_milhas
      
      toast({
        title: 'Pagamento efetuado com sucesso',
        description: 'Suas milhas foram compradas com sucesso!',
        variant: 'default',
        duration: 2000,
      })

      router.push('/reservas')
    } else {
      toast({
        title: 'Erro',
        description: response.message,
        variant: 'destructive',
        duration: 2000,
      })
    }
  } catch (error) {
    const errorMessage = error instanceof Error 
      ? error.message 
      : 'Falha ao processar compra de milhas'
    toast({
      title: 'Erro',
      description: errorMessage,
      variant: 'destructive',
      duration: 2000,
    })
  } finally {
    isLoading.value = false
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
    <Button @click="onSubmit" class="w-full mt-6" :disabled="isLoading">
      <span v-if="!isLoading">
        Pagar {{ totalPrice.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) }}
      </span>
      <span v-else>Processando...</span>
    </Button>
  </div>
</template>