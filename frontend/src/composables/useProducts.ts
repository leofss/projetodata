import { ref, onMounted } from 'vue'
import type { Product, ProductionSuggestion } from '../types'
import * as api from '../services/productService'
import { useI18n } from './useI18n'

const { translateOrFallback } = useI18n()

const addToast = (message: string, fallbackKey?: string, errorCode?: string) => {
  ;(window as any).$toast.addToast(translateOrFallback(message, fallbackKey, errorCode))
}

export function useProducts() {
  const products = ref<Product[]>([])
  const loading = ref(false)

  const fetchProducts = async () => {
    loading.value = true
    try {
      products.value = await api.getProducts()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.fetchProducts',
        'errors.fetchProducts',
        err.response?.data?.errorCode,
      )
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  const addProduct = async (data: Parameters<typeof api.createProduct>[0]) => {
    try {
      await api.createProduct(data)
      await fetchProducts()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.addProduct',
        'errors.addProduct',
        err.response?.data?.errorCode,
      )
      console.error(err)
      throw err
    }
  }

  const updateProduct = async (uuid: string, data: Parameters<typeof api.updateProduct>[1]) => {
    try {
      await api.updateProduct(uuid, data)
      await fetchProducts()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.updateProduct',
        'errors.updateProduct',
        err.response?.data?.errorCode,
      )
      console.error(err)
      throw err
    }
  }

  const removeProduct = async (uuid: string) => {
    try {
      await api.deleteProduct(uuid)
      await fetchProducts()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.deleteProduct',
        'errors.deleteProduct',
        err.response?.data?.errorCode,
      )
      console.error(err)
      throw err
    }
  }

  onMounted(fetchProducts)

  return {
    products,
    loading,
    fetchProducts,
    addProduct,
    updateProduct,
    removeProduct,
  }
}

export function useProductionSuggestions() {
  const suggestions = ref<ProductionSuggestion[]>([])
  const loading = ref(false)

  const fetchSuggestions = async () => {
    loading.value = true
    try {
      suggestions.value = await api.getProductionSuggestions()
    } catch (err: any) {
      addToast(
        err.response?.data?.message || 'errors.fetchProductionSuggestions',
        'errors.fetchProductionSuggestions',
        err.response?.data?.errorCode,
      )
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  return {
    suggestions,
    loading,
    fetchSuggestions,
  }
}
