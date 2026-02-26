<template>
  <div class="raw-materials">
    <h2>{{ t('rawMaterials.title') }}</h2>
    <div class="language-selector">
      <select
        v-model="currentLang"
        @change="setLang(($event.target as HTMLSelectElement).value as 'pt' | 'en')"
      >
        <option value="pt">PT-BR</option>
        <option value="en">EN-US</option>
      </select>
    </div>
    <button @click="openAddModal" class="add-button">{{ t('rawMaterials.addButton') }}</button>
    <div v-if="loading" class="loading">{{ t('rawMaterials.loading') }}</div>
    <div v-else class="list" :class="{ scrollable: sortedRawMaterials.length > 10 }">
      <div v-for="material in sortedRawMaterials" :key="material.uuid" class="item">
        <span class="item-text"
          >{{ truncate(material.code, 15) }} - {{ truncate(material.name, 15) }} -
          {{ material.quantity }}
          {{ translateMeasurement(material.measurement_type, material.quantity) }}
        </span>
        <div class="actions">
          <button @click="openEditModal(material)" class="edit-button">
            {{ t('rawMaterials.form.edit') }}
          </button>
          <button @click="openDeleteModal(material)" class="delete-button">
            {{ t('rawMaterials.form.delete') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Add/Edit Modal -->
    <vue-final-modal
      v-model="showModal"
      teleport-to="body"
      overlay-class="vfm-overlay"
      content-class="vfm-content"
    >
      <div class="modal-content">
        <h3>
          {{ isEditMode ? t('rawMaterials.editModal.title') : t('rawMaterials.addModal.title') }}
        </h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="modal-name">{{ t('rawMaterials.form.name') }}</label>
            <input id="modal-name" v-model="modalForm.name" type="text" maxlength="100" required />
          </div>
          <div class="form-group code-group">
            <label for="modal-code">{{ t('rawMaterials.form.code') }}</label>
            <input
              id="modal-code"
              v-model="modalForm.code"
              type="text"
              maxlength="100"
              @input="validateCode(modalForm.code)"
              required
            />
          </div>
          <div class="form-group">
            <label for="modal-quantity">{{ t('rawMaterials.form.quantity') }}</label>
            <input
              id="modal-quantity"
              v-model.number="modalForm.quantity"
              type="number"
              step="0.01"
              @input="validateQuantity(modalForm.quantity)"
              required
            />
          </div>
          <div class="form-group">
            <label for="modal-measurement">{{ t('rawMaterials.form.measurementType') }}</label>
            <select id="modal-measurement" v-model="modalForm.measurement_type" required>
              <option value="WEIGHT">{{ t('common.weight.singular') }}</option>
              <option value="UNIT">{{ t('common.unit.singular') }}</option>
            </select>
          </div>
          <span v-if="modalErrors" class="error">{{ modalErrors }}</span>

          <div class="modal-actions">
            <button class="action-button" type="button" @click="closeModal">
              {{ t('rawMaterials.form.cancel') }}
            </button>
            <button
              type="submit"
              :disabled="!isFormValid"
              :class="['save-button', { transparente: !isFormValid }]"
            >
              {{ isEditMode ? t('rawMaterials.form.edit') : t('rawMaterials.form.save') }}
            </button>
          </div>
        </form>
      </div>
    </vue-final-modal>

    <!-- Delete Modal -->
    <vue-final-modal v-model="showDeleteModal">
      <div class="modal-content">
        <h3>{{ t('rawMaterials.deleteModal.title') }}</h3>
        <p>{{ t('rawMaterials.deleteModal.message', { name: deleteMaterial?.name || '' }) }}</p>
        <div class="modal-actions">
          <button class="action-button" @click="closeDeleteModal">
            {{ t('rawMaterials.form.cancel') }}
          </button>
          <button @click="handleDelete" class="delete-button">
            {{ t('rawMaterials.form.delete') }}
          </button>
        </div>
      </div>
    </vue-final-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { VueFinalModal } from 'vue-final-modal'
import type { RawMaterial, RawMaterialForm } from '../types'
import { useRawMaterials } from '../composables/useRawMaterials'
import {
  validateCode as validateCodeUtil,
  validateQuantity as validateQuantityUtil,
} from '../utils/validation'
import { useI18n } from '../composables/useI18n'
import '../styles/RawMaterials.css'
import '../styles/App.css'

const { rawMaterials, loading, addRawMaterial, updateRawMaterial, removeRawMaterial } =
  useRawMaterials()

const { t, currentLang, setLang, translateMeasurement } = useI18n()

const showModal = ref(false)
const isEditMode = ref(false)
const showDeleteModal = ref(false)
const deleteMaterial = ref<RawMaterial | null>(null)
const editMaterial = ref<RawMaterial | null>(null)

const modalForm = ref<RawMaterialForm>({
  name: '',
  code: '',
  quantity: 0,
  measurement_type: 'WEIGHT',
})

const codeError = ref<string | undefined>()
const quantityError = ref<string | undefined>()
const modalErrors = computed(() => codeError.value || quantityError.value)

const validateCode = (code: string) => {
  if (!validateCodeUtil(code)) {
    codeError.value = t('rawMaterials.validation.code')
  } else {
    codeError.value = undefined
  }
}

const validateQuantity = (quantity: number) => {
  if (!validateQuantityUtil(quantity)) {
    quantityError.value = t('rawMaterials.validation.quantity')
  } else {
    quantityError.value = undefined
  }
}

const isFormValid = computed(() => {
  return Boolean(
    modalForm.value.name &&
    modalForm.value.code &&
    !modalErrors.value &&
    modalForm.value.quantity >= 0 &&
    modalForm.value.measurement_type,
  )
})

const openAddModal = () => {
  isEditMode.value = false
  modalForm.value = { name: '', code: '', quantity: 0, measurement_type: 'WEIGHT' }
  codeError.value = undefined
  quantityError.value = undefined
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditMode.value = false
  editMaterial.value = null
  codeError.value = undefined
  quantityError.value = undefined
}

const handleSubmit = async () => {
  if (!isFormValid.value) return
  try {
    if (isEditMode.value) {
      await updateRawMaterial(editMaterial.value!.uuid, modalForm.value)
    } else {
      await addRawMaterial(modalForm.value)
    }
    closeModal()
  } catch {}
}

const openEditModal = (material: RawMaterial) => {
  isEditMode.value = true
  editMaterial.value = material
  modalForm.value = {
    name: material.name,
    code: material.code,
    quantity: material.quantity,
    measurement_type: material.measurement_type,
  }
  codeError.value = undefined
  quantityError.value = undefined
  validateCode(modalForm.value.code)
  validateQuantity(modalForm.value.quantity)
  showModal.value = true
}

const openDeleteModal = (material: RawMaterial) => {
  deleteMaterial.value = material
  showDeleteModal.value = true
}

const closeDeleteModal = () => {
  showDeleteModal.value = false
  deleteMaterial.value = null
}

const handleDelete = async () => {
  if (!deleteMaterial.value) return
  try {
    await removeRawMaterial(deleteMaterial.value.uuid)
    closeDeleteModal()
  } catch {}
}

const truncate = (text: string, length: number): string => {
  return text.length > length ? text.substring(0, length) + '...' : text
}

const sortedRawMaterials = computed(() =>
  [...rawMaterials.value].sort((a, b) => a.name.localeCompare(b.name)),
)
</script>
