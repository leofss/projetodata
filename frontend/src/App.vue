<script setup lang="ts">
import RawMaterials from './components/RawMaterials.vue'
import Products from './components/Products.vue'
import Toast from './components/Toast.vue'
import './styles/App.css'
import { useToast } from './composables/useToast'
import { ModalsContainer } from 'vue-final-modal'
const { toasts, removeToast } = useToast()

;(window as any).$toast = { addToast: useToast().addToast }
</script>

<template>
  <div class="app">
    <header class="header">
      <h1>ProjetoData - Teste Técnico</h1>
    </header>
    <div class="content">
      <div class="left-section">
        <RawMaterials />
      </div>
      <div class="divider"></div>
      <div class="right-section">
        <Products />
      </div>
    </div>
    <div class="toast-container">
      <Toast
        v-for="toast in toasts"
        :key="toast.id"
        :message="toast.message"
        @close="removeToast(toast.id)"
      />
    </div>
  </div>
</template>
