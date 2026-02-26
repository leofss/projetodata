<template>
  <div class="products">
    <h2>{{ t('products.title') }}</h2>
    <button @click="openAddModal" class="add-button">{{ t('products.addButton') }}</button>
    <button @click="openPlanningModal" class="planning-button">
      {{ t('products.planningButton') }}
    </button>
    <div v-if="loading" class="loading">{{ t('products.loading') }}</div>
    <div v-else class="list" :class="{ scrollable: sortedProducts.length > 10 }">
      <div v-for="product in sortedProducts" :key="product.uuid" class="item">
        <span class="item-text"
          >{{ truncate(product.name, 15) }} - {{ truncate(product.code, 15) }} - R$
          {{ product.price.toFixed(2) }}</span
        >
        <div class="actions">
          <button @click="openViewCompositionsModal(product)" class="view-button">
            {{ t('products.form.viewCompositions') }}
          </button>
          <button @click="openEditModal(product)" class="edit-button">
            {{ t('products.form.edit') }}
          </button>
          <button @click="openDeleteModal(product)" class="delete-button">
            {{ t('products.form.delete') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Add/Edit Modal -->
    <vue-final-modal v-model="showModal">
      <div class="modal-content">
        <h3>{{ isEditMode ? t('products.editModal.title') : t('products.addModal.title') }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="modal-name">{{ t('products.form.name') }}</label>
            <input id="modal-name" v-model="modalForm.name" type="text" maxlength="100" required />
          </div>
          <div class="form-group code-group">
            <label for="modal-code">{{ t('products.form.code') }}</label>
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
            <label for="modal-price">{{ t('products.form.price') }}</label>
            <input
              id="modal-price"
              v-model.number="modalForm.price"
              type="number"
              step="0.01"
              @input="validateQuantity(modalForm.price)"
              required
            />
          </div>
          <div class="form-group compositions">
            <label>{{ t('products.form.compositions') }}</label>
            <div
              v-for="(comp, index) in modalForm.compositions"
              :key="index"
              class="composition-item"
            >
              <select v-model="comp.raw_material_uuid" required>
                <option v-for="rm in rawMaterials" :key="rm.uuid" :value="rm.uuid">
                  {{ rm.name }}
                </option>
              </select>
              <input
                v-model.number="comp.quantity_required"
                type="number"
                step="0.01"
                :placeholder="t('products.form.quantityRequired')"
                required
              />
              <button
                type="button"
                @click="removeComposition(modalForm.compositions, index)"
                class="remove-composition-button"
              >
                {{ t('products.form.removeComposition') }}
              </button>
            </div>
            <button
              type="button"
              @click="addComposition(modalForm.compositions)"
              class="add-composition-button"
            >
              {{ t('products.form.addComposition') }}
            </button>
          </div>
          <span v-if="modalCodeErrors" class="error">{{ modalCodeErrors }}</span>

          <div class="modal-actions">
            <button class="action-button" type="button" @click="closeModal">
              {{ t('products.form.cancel') }}
            </button>
            <button
              type="submit"
              :disabled="!isFormValid"
              :class="[isEditMode ? 'edit-button' : 'save-button', { transparente: !isFormValid }]"
            >
              {{ isEditMode ? t('products.form.edit') : t('products.form.save') }}
            </button>
          </div>
        </form>
      </div>
    </vue-final-modal>

    <!-- Delete Modal -->
    <vue-final-modal v-model="showDeleteModal">
      <div class="modal-content">
        <h3>{{ t('products.deleteModal.title') }}</h3>
        <p>{{ t('products.deleteModal.message', { name: deleteProduct?.name || '' }) }}</p>
        <div class="modal-actions">
          <button class="action-button" @click="closeDeleteModal">
            {{ t('products.form.cancel') }}
          </button>
          <button @click="handleDelete" class="delete-button">
            {{ t('products.form.delete') }}
          </button>
        </div>
      </div>
    </vue-final-modal>

    <!-- View Compositions Modal -->
    <vue-final-modal v-model="showViewCompositionsModal">
      <div class="modal-content">
        <h3>{{ t('products.viewCompositions.title', { name: viewProduct?.name || '' }) }}</h3>
        <div class="compositions-list" :class="{ scrollable: isCompositionsScrollable }">
          <div
            v-for="comp in viewProduct?.compositions"
            :key="comp.raw_material.uuid"
            class="composition-item"
          >
            <span>
              {{ comp.raw_material.name }} - {{ comp.quantity_required }}
              {{
                translateMeasurement(comp.raw_material.measurement_type, comp.quantity_required)
              }}</span
            >
          </div>
        </div>
        <div class="modal-actions">
          <button class="action-button" @click="closeViewCompositionsModal">
            {{ t('products.form.close') }}
          </button>
        </div>
      </div>
    </vue-final-modal>

    <!-- Planning Modal -->
    <vue-final-modal v-model="showPlanningModal">
      <div class="modal-content">
        <h3>{{ t('products.planningModal.title') }}</h3>
        <div v-if="suggestionsLoading" class="loading">{{ t('products.loading') }}</div>
        <div v-else class="suggestions-list" :class="{ scrollable: suggestions.length > 10 }">
          <div v-for="sug in suggestions" :key="sug.product_uuid" class="suggestion-item">
            <p>
              <strong>{{ sug.product_name }}</strong>
            </p>
            <p>{{ t('products.planning.unitPrice') }}: R$ {{ sug.unit_price.toFixed(2) }}</p>
            <p>{{ t('products.planning.maxQuantity') }}: {{ sug.max_production_quantity }}</p>
            <p>{{ t('products.planning.totalRevenue') }}: R$ {{ sug.total_revenue.toFixed(2) }}</p>
          </div>
        </div>
        <div class="modal-actions">
          <button class="action-button" @click="closePlanningModal">
            {{ t('products.form.close') }}
          </button>
        </div>
      </div>
    </vue-final-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { VueFinalModal } from 'vue-final-modal'
import type { Product, ProductForm, ProductCompositionForm } from '../types'
import { useProducts, useProductionSuggestions } from '../composables/useProducts'
import { useRawMaterials } from '../composables/useRawMaterials'
import {
  validateCode as validateCodeUtil,
  validateQuantity as validateQuantityUtil,
} from '../utils/validation'
import { useI18n } from '../composables/useI18n'
import '../styles/Products.css'
import '../styles/App.css'

const { products, loading, addProduct, updateProduct, removeProduct } = useProducts()
const { rawMaterials } = useRawMaterials()
const { suggestions, loading: suggestionsLoading, fetchSuggestions } = useProductionSuggestions()

const { t, translateMeasurement } = useI18n()

const showModal = ref(false)
const isEditMode = ref(false)
const showDeleteModal = ref(false)
const showViewCompositionsModal = ref(false)
const showPlanningModal = ref(false)
const deleteProduct = ref<Product | null>(null)
const editProduct = ref<Product | null>(null)
const viewProduct = ref<Product | null>(null)

const modalForm = ref<ProductForm>({
  name: '',
  code: '',
  price: 0,
  compositions: [],
})

const codeError = ref<string | undefined>()
const quantityError = ref<string | undefined>()
const modalCodeErrors = computed(() => codeError.value || quantityError.value)

const isCompositionsScrollable = computed(
  () => (viewProduct?.value?.compositions?.length ?? 0) > 10,
)

const validateCode = (code: string) => {
  if (!validateCodeUtil(code)) {
    codeError.value = t('products.validation.code')
  } else {
    codeError.value = undefined
  }
}

const validateQuantity = (quantity: number) => {
  if (!validateQuantityUtil(quantity)) {
    quantityError.value = t('products.validation.quantity')
  } else {
    quantityError.value = undefined
  }
}

const isFormValid = computed(() => {
  return Boolean(
    modalForm.value.name &&
    modalForm.value.code &&
    !modalCodeErrors.value &&
    modalForm.value.price > 0 &&
    modalForm.value.compositions.length > 0 &&
    modalForm.value.compositions.every((c) => c.raw_material_uuid && c.quantity_required > 0),
  )
})

const addComposition = (compositions: ProductCompositionForm[]) => {
  compositions.push({ raw_material_uuid: '', quantity_required: 0 })
}

const removeComposition = (compositions: ProductCompositionForm[], index: number) => {
  compositions.splice(index, 1)
}

const openAddModal = () => {
  isEditMode.value = false
  modalForm.value = { name: '', code: '', price: 0, compositions: [] }
  codeError.value = undefined
  quantityError.value = undefined
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  isEditMode.value = false
  editProduct.value = null
  codeError.value = undefined
  quantityError.value = undefined
}

const handleSubmit = async () => {
  if (!isFormValid.value) return
  try {
    if (isEditMode.value) {
      await updateProduct(editProduct.value!.uuid, modalForm.value)
    } else {
      await addProduct(modalForm.value)
    }
    closeModal()
  } catch {}
}

const openEditModal = (product: Product) => {
  isEditMode.value = true
  editProduct.value = product
  modalForm.value = {
    name: product.name,
    code: product.code,
    price: product.price,
    compositions: product.compositions.map((c) => ({
      raw_material_uuid: c.raw_material.uuid,
      quantity_required: c.quantity_required,
    })),
  }
  codeError.value = undefined
  quantityError.value = undefined
  validateCode(modalForm.value.code)
  validateQuantity(modalForm.value.price)
  showModal.value = true
}

const openDeleteModal = (product: Product) => {
  deleteProduct.value = product
  showDeleteModal.value = true
}

const closeDeleteModal = () => {
  showDeleteModal.value = false
  deleteProduct.value = null
}

const handleDelete = async () => {
  if (!deleteProduct.value) return
  try {
    await removeProduct(deleteProduct.value.uuid)
    closeDeleteModal()
  } catch {}
}

const openViewCompositionsModal = (product: Product) => {
  viewProduct.value = product
  showViewCompositionsModal.value = true
}

const closeViewCompositionsModal = () => {
  showViewCompositionsModal.value = false
  viewProduct.value = null
}

const openPlanningModal = async () => {
  showPlanningModal.value = true
  await fetchSuggestions()
}

const closePlanningModal = () => {
  showPlanningModal.value = false
}

const truncate = (text: string, length: number): string => {
  return text.length > length ? text.substring(0, length) + '...' : text
}

const sortedProducts = computed(() =>
  [...products.value].sort((a, b) => a.name.localeCompare(b.name)),
)
</script>
