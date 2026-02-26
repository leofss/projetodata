<template>
  <div v-if="visible" class="toast" :class="{ show: visible }">
    <div class="toast-header">
      <strong class="me-auto">{{ t('errors.erro') }}</strong>
      <button type="button" class="btn-close" @click="close"></button>
    </div>
    <div class="toast-body">
      {{ message }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from '../composables/useI18n'

interface Props {
  message: string
}

const props = defineProps<Props>()

const visible = ref(true)

const { t } = useI18n()

const emit = defineEmits<{
  close: []
}>()

const close = () => {
  visible.value = false
  setTimeout(() => {
    emit('close')
  }, 300)
}

onMounted(() => {
  setTimeout(() => {
    close()
  }, 5000)
})
</script>

<style scoped>
.toast {
  position: fixed;
  top: 20px;
  right: 20px;
  width: 300px;
  background-color: #dc3545;
  color: white;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  opacity: 0;
  transform: translateX(100%);
  transition:
    opacity 0.3s,
    transform 0.3s;
}

.toast.show {
  opacity: 1;
  transform: translateX(0);
}

.toast-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.toast-body {
  padding: 10px 15px;
}

.btn-close {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
}
</style>
