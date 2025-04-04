<template>
  <Dialog v-model:open="isVisible">
    <DialogContent class="sm:max-w-md">
      <DialogHeader>
        <DialogTitle>Confirmar Cancelamento</DialogTitle>
        <br>
        <DialogDescription>
          Tem certeza de que deseja cancelar esta reserva? Esta ação não pode ser desfeita.
        </DialogDescription>
      </DialogHeader>
      <div class="flex justify-end space-x-2">
        <Button @click="confirmCancel" variant="destructive">Confirmar</Button>
        <Button @click="closeDialog">Voltar</Button>
      </div>
    </DialogContent>
  </Dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

const isVisible = ref(false);

const emit = defineEmits(['confirm']);

const openDialog = () => {
  isVisible.value = true;
};

const closeDialog = () => {
  isVisible.value = false;
};

const confirmCancel = () => {
  emit('confirm');
  closeDialog();
};

defineExpose({ openDialog });
</script>
