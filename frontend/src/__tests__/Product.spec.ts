import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import Products from '../components/Products.vue'

const addProduct = vi.fn()
const updateProduct = vi.fn()
const removeProduct = vi.fn()

const mockProducts = [
  {
    uuid: '1',
    name: 'Product A',
    code: 'PA001',
    price: 100,
    compositions: [],
  },
  {
    uuid: '2',
    name: 'Product B',
    code: 'PB001',
    price: 200,
    compositions: [],
  },
]

vi.mock('../composables/useProducts', () => ({
  useProducts: () => ({
    products: { value: mockProducts },
    loading: { value: false },
    addProduct,
    updateProduct,
    removeProduct,
  }),
  useProductionSuggestions: () => ({
    suggestions: { value: [] },
    loading: { value: false },
    fetchSuggestions: vi.fn(),
  }),
}))

vi.mock('../composables/useRawMaterials', () => ({
  useRawMaterials: () => ({
    rawMaterials: { value: [] },
  }),
}))

vi.mock('../composables/useI18n', () => ({
  useI18n: () => ({
    t: (key: string) => key,
    currentLang: { value: 'en' },
    setLang: vi.fn(),
    translateMeasurement: vi.fn(),
  }),
}))

vi.mock('vue-final-modal', () => ({
  VueFinalModal: {
    template: '<div><slot /></div>',
  },
}))

describe('Products.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render without crashing', () => {
    const wrapper = mount(Products)
    expect(wrapper.exists()).toBe(true)
  })

  it('should open modal when add button is clicked', async () => {
    const wrapper = mount(Products)

    const button = wrapper.find('.add-button')
    await button.trigger('click')

    expect(wrapper.vm.showModal).toBe(true)
  })

  it('should submit when form is valid', async () => {
    const wrapper = mount(Products)

    wrapper.vm.modalForm.name = 'New Product'
    wrapper.vm.modalForm.code = 'NP001'
    wrapper.vm.modalForm.price = 50
    wrapper.vm.modalForm.compositions = [{ raw_material_uuid: '1', quantity_required: 1 }]

    await wrapper.vm.handleSubmit()

    expect(addProduct).toHaveBeenCalled()
  })

  it('should edit product', async () => {
    const wrapper = mount(Products)

    const product = mockProducts[0]

    wrapper.vm.openEditModal(product)

    wrapper.vm.modalForm.name = 'Updated Product'
    wrapper.vm.modalForm.compositions = [{ raw_material_uuid: '1', quantity_required: 1 }]

    await wrapper.vm.handleSubmit()

    expect(updateProduct).toHaveBeenCalledWith(
      product.uuid,
      expect.objectContaining({
        name: 'Updated Product',
      }),
    )
  })

  it('should delete product', async () => {
    const wrapper = mount(Products)

    const product = mockProducts[0]

    wrapper.vm.deleteProduct = product

    await wrapper.vm.handleDelete()

    expect(removeProduct).toHaveBeenCalledWith(product.uuid)
  })

  it('should not submit if form is invalid', async () => {
    const wrapper = mount(Products)

    wrapper.vm.modalForm.name = ''
    wrapper.vm.modalForm.code = ''
    wrapper.vm.modalForm.price = 0
    wrapper.vm.modalForm.compositions = []

    await wrapper.vm.handleSubmit()

    expect(addProduct).not.toHaveBeenCalled()
  })
})
