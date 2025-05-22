<script setup lang="ts">
import {
  NumberField,
  NumberFieldContent,
  NumberFieldDecrement,
  NumberFieldIncrement,
  NumberFieldInput,
} from '@/components/ui/number-field'

defineProps<{
  modelValue: number
  max: number
}>()

defineEmits<{
  'update:modelValue': [value: number]
}>()
</script>

<template>
  <NumberField
    id="seat"
    :model-value="modelValue"
    @update:modelValue="$emit('update:modelValue', $event)"
    :min="1"
    :max="max"
    :disabled="max === 0"
  >
    <label for="seat">Quantidade de poltronas:</label>
    <NumberFieldContent>
      <NumberFieldDecrement />
      <NumberFieldInput />
      <NumberFieldIncrement />
    </NumberFieldContent>
  </NumberField>
</template>
