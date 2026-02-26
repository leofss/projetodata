import type { Product, ProductForm, ProductionSuggestion, PaginatedResponse } from '../types'
import { api } from './api'

export const getProducts = async (): Promise<Product[]> => {
  const response = await api.get<PaginatedResponse<Product>>('/product')
  return response.data.content
}

export const createProduct = async (data: ProductForm): Promise<Product> => {
  const response = await api.post<Product>('/product', data)
  return response.data
}

export const updateProduct = async (uuid: string, data: ProductForm): Promise<Product> => {
  const response = await api.put<Product>(`/product/${uuid}`, data)
  return response.data
}

export const deleteProduct = async (uuid: string): Promise<void> => {
  await api.delete(`/product/${uuid}`)
}

export const getProductionSuggestions = async (): Promise<ProductionSuggestion[]> => {
  const response = await api.get<ProductionSuggestion[]>('/production-planning/suggestions')
  return response.data
}
