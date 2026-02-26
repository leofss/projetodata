import { ref, onMounted } from 'vue'
import type { RawMaterial } from '../types'
import * as api from '../services/rawMaterialService'
import { useI18n } from './useI18n'

const { translateOrFallback } = useI18n()

const addToast = (message: string, fallbackKey?: string, errorCode?: string) => {
  ;(window as any).$toast.addToast(translateOrFallback(message, fallbackKey, errorCode))
}

export function useRawMaterials() {
  const rawMaterials = ref<RawMaterial[]>([])
  const loading = ref(false)

  const fetchRawMaterials = async () => {
    loading.value = true
    try {
      rawMaterials.value = await api.getRawMaterials()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.fetchRawMaterials',
        'errors.fetchRawMaterials',
        err.response?.data?.errorCode,
      )
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  const addRawMaterial = async (data: Parameters<typeof api.createRawMaterial>[0]) => {
    try {
      await api.createRawMaterial(data)
      await fetchRawMaterials()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.addRawMaterial',
        'errors.addRawMaterial',
        err.response?.data?.errorCode,
      )
      console.error(err)
      throw err
    }
  }

  const updateRawMaterial = async (
    uuid: string,
    data: Parameters<typeof api.updateRawMaterial>[1],
  ) => {
    try {
      await api.updateRawMaterial(uuid, data)
      await fetchRawMaterials()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.updateRawMaterial',
        'errors.updateRawMaterial',
        err.response?.data?.errorCode,
      )
      console.error(err)
      throw err
    }
  }

  const removeRawMaterial = async (uuid: string) => {
    try {
      await api.deleteRawMaterial(uuid)
      await fetchRawMaterials()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.deleteRawMaterial',
        'errors.deleteRawMaterial',
        err.response?.data?.errorCode,
      )
      console.error(err)
      throw err
    }
  }

  onMounted(fetchRawMaterials)

  return {
    rawMaterials,
    loading,
    fetchRawMaterials,
    addRawMaterial,
    updateRawMaterial,
    removeRawMaterial,
  }
}
