import type { RawMaterial, RawMaterialForm, PaginatedResponse } from '../types'
import { api } from './api'

export const getRawMaterials = async (): Promise<RawMaterial[]> => {
  const response = await api.get<PaginatedResponse<RawMaterial>>('/raw-material')
  return response.data.content
}

export const createRawMaterial = async (data: RawMaterialForm): Promise<RawMaterial> => {
  const response = await api.post<RawMaterial>('/raw-material', data)
  return response.data
}

export const updateRawMaterial = async (
  uuid: string,
  data: RawMaterialForm,
): Promise<RawMaterial> => {
  const response = await api.put<RawMaterial>(`/raw-material/${uuid}`, data)
  return response.data
}

export const deleteRawMaterial = async (uuid: string): Promise<void> => {
  await api.delete(`/raw-material/${uuid}`)
}
