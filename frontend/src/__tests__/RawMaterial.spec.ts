import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import RawMaterial from '../components/RawMaterials.vue'

const addRawMaterial = vi.fn()
const updateRawMaterial = vi.fn()
const removeRawMaterial = vi.fn()

const mockRawMaterials = [
  {
    uuid: '1',
    name: 'Iron',
    code: 'IR001',
    quantity: 10,
    measurement_type: 'WEIGHT',
  },
  {
    uuid: '2',
    name: 'Aluminum',
    code: 'AL001',
    quantity: 5,
    measurement_type: 'UNIT',
  },
]

vi.mock('../composables/useRawMaterials', () => ({
  useRawMaterials: () => ({
    rawMaterials: { value: mockRawMaterials },
    loading: { value: false },
    addRawMaterial,
    updateRawMaterial,
    removeRawMaterial,
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

describe('RawMaterial.vue', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render without crashing', () => {
    const wrapper = mount(RawMaterial)
    expect(wrapper.exists()).toBe(true)
  })

  it('should open modal when add button is clicked', async () => {
    const wrapper = mount(RawMaterial)

    const button = wrapper.find('.add-button')
    await button.trigger('click')

    expect(wrapper.vm.showModal).toBe(true)
  })

  it('should submit when form is valid', async () => {
    const wrapper = mount(RawMaterial)

    wrapper.vm.modalForm.name = 'Steel'
    wrapper.vm.modalForm.code = 'ST001'
    wrapper.vm.modalForm.quantity = 10
    wrapper.vm.modalForm.measurement_type = 'WEIGHT'

    await wrapper.vm.handleSubmit()

    expect(addRawMaterial).toHaveBeenCalled()
  })

  it('should edit raw material', async () => {
    const wrapper = mount(RawMaterial)

    const material = mockRawMaterials[0]

    wrapper.vm.openEditModal(material)

    wrapper.vm.modalForm.name = 'Iron Updated'

    await wrapper.vm.handleSubmit()

    expect(updateRawMaterial).toHaveBeenCalledWith(
      material.uuid,
      expect.objectContaining({
        name: 'Iron Updated',
      }),
    )
  })

  it('should delete raw material', async () => {
    const wrapper = mount(RawMaterial)

    const material = mockRawMaterials[0]

    wrapper.vm.deleteMaterial = material

    await wrapper.vm.handleDelete()

    expect(removeRawMaterial).toHaveBeenCalledWith(material.uuid)
  })

  it('should not submit if form is invalid', async () => {
    const wrapper = mount(RawMaterial)

    wrapper.vm.modalForm.name = ''
    wrapper.vm.modalForm.code = ''
    wrapper.vm.modalForm.quantity = -1

    await wrapper.vm.handleSubmit()

    expect(addRawMaterial).not.toHaveBeenCalled()
  })
})
