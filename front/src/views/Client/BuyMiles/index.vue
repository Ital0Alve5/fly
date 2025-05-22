<script setup lang="ts">
import { ResizablePanelGroup, ResizablePanel } from '@/components/ui/resizable'
import MilesQuantity from './components/MilesQuantity.vue'
</script>

<template>
  <div class="flex h-screen w-full items-center justify-center p-8 bg-gray-100">
    <ResizablePanelGroup
      id="miles-panel-group"
      direction="horizontal"
      class="w-full max-w-3xl bg-white rounded-xl shadow-md overflow-hidden"
      :initialSizes="[40, 60]"
      :minSizes="[200, 200]"
    >
      <ResizablePanel id="miles-input-panel" class="border-r border-gray-200">
        <div class="flex h-full items-center justify-center p-6">
          <div class="w-full max-w-sm">
            <MilesQuantity />
          </div>
        </div>
      </ResizablePanel>
      <ResizablePanel id="miles-detail-panel">
      </ResizablePanel>
    </ResizablePanelGroup>
  </div>
</template>
