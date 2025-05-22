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
    id="miles"
    :model-value="modelValue"
    @update:modelValue="$emit('update:modelValue', $event)"
    :min="0"
    :max="max"
    :disabled="max === 0"
  >
    <label for="miles">Milhas a usar:</label>
    <NumberFieldContent>
      <NumberFieldDecrement />
      <NumberFieldInput />
      <NumberFieldIncrement />
    </NumberFieldContent>
  </NumberField>
</template>
