<script setup lang="ts">
import {
  ResizableHandle,
  ResizablePanel,
  ResizablePanelGroup,
} from '../../../components/ui/resizable'
import MilesQuantity from './components/MilesQuantity.vue'
import MilesCheckout from './components/MilesCheckout.vue'
</script>

<template>
  <div class="flex h-screen w-full items-center justify-center p-20">
    <ResizablePanelGroup
      id="demo-group-1"
      direction="horizontal"
      class="w-full max-w-full rounded-lg border shadow-sm overflow-hidden"
    >
      <ResizablePanel id="demo-panel-1" :default-size="40" class="bg-black">
        <div class="flex h-full items-center justify-center p-6">
          <div class="w-full max-w-md">
            <MilesQuantity />
          </div>
        </div>
      </ResizablePanel>

      <ResizableHandle id="demo-handle-1" />

      <ResizablePanel id="demo-panel-2" :default-size="60">
        <ResizablePanelGroup id="demo-group-2" direction="vertical">
          <ResizablePanel id="demo-panel-4" :default-size="75" class="bg-black">
            <div class="flex h-full items-center justify-center p-6">
              <div class="w-full max-w-md">
                <MilesCheckout />
              </div>
            </div>
          </ResizablePanel>
        </ResizablePanelGroup>
      </ResizablePanel>
    </ResizablePanelGroup>
  </div>
</template>
