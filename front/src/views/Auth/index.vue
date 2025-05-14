<script setup lang="ts">
import { ref } from 'vue'
import { Tabs, TabsList, TabsTrigger, TabsContent } from '@/components/ui/tabs'
import LoginForm from './components/LoginForm.vue'
import RegisterForm from './components/RegisterForm.vue'

const activeTab = ref('login')
</script>

<template>
  <Tabs v-model="activeTab" class="w-[400px] mx-auto">
    <TabsList class="grid grid-cols-2 w-full">
      <TabsTrigger value="login">Login</TabsTrigger>
      <TabsTrigger value="register">Cadastro</TabsTrigger>
    </TabsList>

    <TabsContent value="login">
      <LoginForm />
    </TabsContent>
    <TabsContent value="register">
      <RegisterForm @registered="activeTab = 'login'"/>
    </TabsContent>
  </Tabs>
</template>
