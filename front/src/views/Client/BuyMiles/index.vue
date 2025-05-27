<script setup lang="ts">
import { ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'
import MilesQuantity from './components/MilesQuantity.vue'
</script>

<template>
  <div class="flex h-screen w-full items-center justify-center p-20">
    <ResizablePanelGroup
      id="demo-group-1"
      direction="horizontal"
      class="w-full max-w-[600px] rounded-lg border shadow-sm overflow-hidden"
    >
      <ResizablePanel id="demo-panel-1" class="bg-black">
        <div class="flex h-full items-center justify-center">
          <div class="w-full max-w-md">
            <MilesQuantity />
          </div>
        </div>
      </ResizablePanel>
    </ResizablePanelGroup>
  </div>
</template>
