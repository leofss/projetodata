export interface RawMaterial {
  uuid: string
  code: string
  name: string
  quantity: number
  measurement_type: 'WEIGHT' | 'UNIT'
}

export interface Composition {
  quantity_required: number
  raw_material: RawMaterial
}

export interface Product {
  uuid: string
  name: string
  code: string
  price: number
  compositions: Composition[]
}

export interface ProductionSuggestion {
  product_uuid: string
  product_name: string
  unit_price: number
  max_production_quantity: number
  total_revenue: number
}

export interface PaginatedResponse<T> {
  content: T[]
  first: boolean
  last: boolean
  page: number
  size: number
  page_elements: number
  total_pages: number
  total_elements: number
}

export type MeasurementType = 'WEIGHT' | 'UNIT'

export interface RawMaterialForm {
  name: string
  code: string
  quantity: number
  measurement_type: MeasurementType
}

export interface ProductForm {
  name: string
  code: string
  price: number
  compositions: ProductCompositionForm[]
}

export interface ProductCompositionForm {
  raw_material_uuid: string
  quantity_required: number
}
