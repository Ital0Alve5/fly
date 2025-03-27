import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { vMaska } from 'maska/vue'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.directive('mask', vMaska)

app.mount('#app')
