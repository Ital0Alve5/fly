<script setup lang="ts">
import { ref } from 'vue'
import { Label } from '@/components/ui/label'
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput,
} from '@/components/ui/number-field'
import { useRouter } from 'vue-router'
import { Separator } from '@/components/ui/separator'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const user = ref({
  firstName: authStore.user?.name.split(' ')[0],
  miles: authStore.user?.miles
})

const miles = ref(10)
</script>

<template>
  <div class="h-full w-full flex flex-col border-0 space-y-6">
    <div class="p-6">
      <h2 class="text-lg font-semibold text-center">
        Informações de {{ user.firstName }}
      </h2>
    </div>
    
    <div class="px-6 pb-6">
      <ul class="space-y-4">
        <li>
          <span class="font-medium w-32 shrink-0">Minhas milhas:</span>
          <span class="pl-2">{{ user.miles }}</span>
        </li>
      </ul>
    </div>
    
    <Separator/>
    
    <div class="p-6 pt-12">
      <h2 class="text-lg font-semibold text-center">
        Selecione a quantidade de milhas
      </h2>
      <p class="pt-4 text-center">1 milha = R$5,00</p>
    </div>
    
    <div class="px-6 pb-6">
      <NumberField id="miles" v-model="miles" :min="0">
        <Label>Milhas</Label>
        <NumberFieldContent>
          <NumberFieldDecrement />
          <NumberFieldInput />
          <NumberFieldIncrement />
        </NumberFieldContent>
      </NumberField>
    </div>

    <div class="p-6 pt-12">
      <p class="text-lg pt-4 text-center">{{miles}} milhas custam R${{ miles * 5 }},00</p>
    </div>
  </div>
</template>