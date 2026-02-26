import { ref } from 'vue'

type Lang = 'pt' | 'en'

type TranslationKeys = {
  'app.title': string
  'rawMaterials.title': string
  'rawMaterials.addButton': string
  'rawMaterials.loading': string
  'rawMaterials.addModal.title': string
  'rawMaterials.editModal.title': string
  'rawMaterials.deleteModal.title': string
  'rawMaterials.deleteModal.message': string
  'rawMaterials.form.name': string
  'rawMaterials.form.code': string
  'rawMaterials.form.quantity': string
  'rawMaterials.form.measurementType': string
  'rawMaterials.form.cancel': string
  'rawMaterials.form.save': string
  'rawMaterials.form.edit': string
  'rawMaterials.form.delete': string
  'rawMaterials.validation.code': string
  'rawMaterials.validation.quantity': string
  'products.title': string
  'products.addButton': string
  'products.planningButton': string
  'products.loading': string
  'products.addModal.title': string
  'products.editModal.title': string
  'products.deleteModal.title': string
  'products.deleteModal.message': string
  'products.viewCompositions.title': string
  'products.planningModal.title': string
  'products.form.name': string
  'products.form.code': string
  'products.form.price': string
  'products.form.compositions': string
  'products.form.addComposition': string
  'products.form.removeComposition': string
  'products.form.cancel': string
  'products.form.save': string
  'products.form.edit': string
  'products.form.delete': string
  'products.form.close': string
  'products.form.viewCompositions': string
  'products.form.quantityRequired': string
  'products.planning.unitPrice': string
  'products.planning.maxQuantity': string
  'products.planning.totalRevenue': string
  'products.validation.code': string
  'products.validation.quantity': string
  'common.weight.singular': string
  'common.unit.singular': string
  'common.weight.plural': string
  'common.unit.plural': string
  'errors.fetchRawMaterials': string
  'errors.addRawMaterial': string
  'errors.updateRawMaterial': string
  'errors.deleteRawMaterial': string
  'errors.fetchProducts': string
  'errors.addProduct': string
  'errors.updateProduct': string
  'errors.deleteProduct': string
  'errors.fetchProductionSuggestions': string
  'errors.DUPLICATE_RAW_MATERIAL_IN_PRODUCT': string
  'errors.PRODUCT_CODE_ALREADY_EXISTS': string
  'errors.RAW_MATERIAL_CODE_ALREADY_EXISTS': string
  'errors.PRODUCT_NOT_FOUND': string
  'errors.RAW_MATERIAL_NOT_FOUND': string
  'errors.INVALID_FIELD': string
  'errors.erro': string
}

const currentLang = ref<Lang>('pt')

const translations: Record<Lang, TranslationKeys> = {
  pt: {
    'app.title': 'Product Data',
    'rawMaterials.title': 'Matérias-Primas',
    'rawMaterials.addButton': 'Adicionar Matéria-Prima',
    'rawMaterials.loading': 'Carregando...',
    'rawMaterials.addModal.title': 'Adicionar Matéria-Prima',
    'rawMaterials.editModal.title': 'Editar Matéria-Prima',
    'rawMaterials.deleteModal.title': 'Confirmar Exclusão',
    'rawMaterials.deleteModal.message': 'Deseja deletar a matéria-prima "{{ name }}"?',
    'rawMaterials.form.name': 'Nome:',
    'rawMaterials.form.code': 'Código:',
    'rawMaterials.form.quantity': 'Quantidade:',
    'rawMaterials.form.measurementType': 'Tipo de Medida:',
    'rawMaterials.form.cancel': 'Cancelar',
    'rawMaterials.form.save': 'Salvar',
    'rawMaterials.form.edit': 'Editar',
    'rawMaterials.form.delete': 'Deletar',
    'rawMaterials.validation.code': 'Código deve conter apenas letras maiúsculas e números.',
    'rawMaterials.validation.quantity':
      'Quantidade deve ser um número positivo com até 2 casas decimais e no máximo 17 dígitos antes da vírgula.',
    'products.title': 'Produtos',
    'products.addButton': 'Adicionar Produto',
    'products.planningButton': 'Planejar Produção',
    'products.loading': 'Carregando...',
    'products.addModal.title': 'Adicionar Produto',
    'products.editModal.title': 'Editar Produto',
    'products.deleteModal.title': 'Confirmar Exclusão',
    'products.deleteModal.message': 'Deseja deletar o produto "{{ name }}"?',
    'products.viewCompositions.title': 'Composições de {{ name }}',
    'products.planningModal.title': 'Sugestões de Produção',
    'products.form.name': 'Nome:',
    'products.form.code': 'Código:',
    'products.form.price': 'Preço:',
    'products.form.compositions': 'Composições:',
    'products.form.addComposition': 'Adicionar Composição',
    'products.form.removeComposition': 'Remover',
    'products.form.cancel': 'Cancelar',
    'products.form.save': 'Salvar',
    'products.form.edit': 'Editar',
    'products.form.delete': 'Deletar',
    'products.form.close': 'Fechar',
    'products.form.viewCompositions': 'Ver Composições',
    'products.form.quantityRequired': 'Quantidade Necessária',
    'products.planning.unitPrice': 'Preço Unitário',
    'products.planning.maxQuantity': 'Quantidade Máxima',
    'products.planning.totalRevenue': 'Receita Total',
    'products.validation.code': 'Código deve conter apenas letras maiúsculas e números.',
    'products.validation.quantity':
      'Quantidade deve ser um número positivo com até 2 casas decimais e no máximo 17 dígitos antes da vírgula.',
    'common.weight.singular': 'Grama',
    'common.weight.plural': 'Gramas',
    'common.unit.singular': 'Unidade',
    'common.unit.plural': 'Unidades',
    'errors.fetchRawMaterials': 'Falha ao buscar matérias-primas',
    'errors.addRawMaterial': 'Falha ao adicionar matéria-prima',
    'errors.updateRawMaterial': 'Falha ao atualizar matéria-prima',
    'errors.deleteRawMaterial': 'Falha ao deletar matéria-prima',
    'errors.fetchProducts': 'Falha ao buscar produtos',
    'errors.addProduct': 'Falha ao adicionar produto',
    'errors.updateProduct': 'Falha ao atualizar produto',
    'errors.deleteProduct': 'Falha ao deletar produto',
    'errors.fetchProductionSuggestions': 'Falha ao buscar sugestões de produção',
    'errors.DUPLICATE_RAW_MATERIAL_IN_PRODUCT': 'Matéria-prima duplicada na composição do produto',
    'errors.PRODUCT_CODE_ALREADY_EXISTS': 'Código do produto já existe',
    'errors.RAW_MATERIAL_CODE_ALREADY_EXISTS': 'Código da matéria-prima já existe',
    'errors.PRODUCT_NOT_FOUND': 'Produto não encontrado',
    'errors.RAW_MATERIAL_NOT_FOUND': 'Matéria-prima não encontrada',
    'errors.INVALID_FIELD': 'Campo inválido',
    'errors.erro': 'Erro',
  },
  en: {
    'app.title': 'Product Data',
    'rawMaterials.title': 'Raw Materials',
    'rawMaterials.addButton': 'Add Raw Material',
    'rawMaterials.loading': 'Loading...',
    'rawMaterials.addModal.title': 'Add Raw Material',
    'rawMaterials.editModal.title': 'Edit Raw Material',
    'rawMaterials.deleteModal.title': 'Confirm Deletion',
    'rawMaterials.deleteModal.message': 'Do you want to delete the raw material "{{ name }}"?',
    'rawMaterials.form.name': 'Name:',
    'rawMaterials.form.code': 'Code:',
    'rawMaterials.form.quantity': 'Quantity:',
    'rawMaterials.form.measurementType': 'Measurement Type:',
    'rawMaterials.form.cancel': 'Cancel',
    'rawMaterials.form.save': 'Save',
    'rawMaterials.form.edit': 'Edit',
    'rawMaterials.form.delete': 'Delete',
    'rawMaterials.validation.code': 'Code must contain only uppercase letters and numbers.',
    'rawMaterials.validation.quantity':
      'Quantity must be a positive number with up to 2 decimal places and a maximum of 17 digits before the decimal point.',
    'products.title': 'Products',
    'products.addButton': 'Add Product',
    'products.planningButton': 'Plan Production',
    'products.loading': 'Loading...',
    'products.addModal.title': 'Add Product',
    'products.editModal.title': 'Edit Product',
    'products.deleteModal.title': 'Confirm Deletion',
    'products.deleteModal.message': 'Do you want to delete the product "{{ name }}"?',
    'products.viewCompositions.title': 'Compositions of {{ name }}',
    'products.planningModal.title': 'Production Suggestions',
    'products.form.name': 'Name:',
    'products.form.code': 'Code:',
    'products.form.price': 'Price:',
    'products.form.compositions': 'Compositions:',
    'products.form.addComposition': 'Add Composition',
    'products.form.removeComposition': 'Remove',
    'products.form.cancel': 'Cancel',
    'products.form.save': 'Save',
    'products.form.edit': 'Edit',
    'products.form.delete': 'Delete',
    'products.form.close': 'Close',
    'products.form.viewCompositions': 'View Compositions',
    'products.form.quantityRequired': 'Quantity Required',
    'products.planning.unitPrice': 'Unit Price',
    'products.planning.maxQuantity': 'Maximum Quantity',
    'products.planning.totalRevenue': 'Total Revenue',
    'products.validation.code': 'Code must contain only uppercase letters and numbers.',
    'products.validation.quantity':
      'Quantity must be a positive number with up to 2 decimal places and a maximum of 17 digits before the decimal point.',

    'common.weight.singular': 'Gram',
    'common.weight.plural': 'Grams',
    'common.unit.singular': 'Unit',
    'common.unit.plural': 'Units',
    'errors.fetchRawMaterials': 'Failed to fetch raw materials',
    'errors.addRawMaterial': 'Failed to add raw material',
    'errors.updateRawMaterial': 'Failed to update raw material',
    'errors.deleteRawMaterial': 'Failed to delete raw material',
    'errors.fetchProducts': 'Failed to fetch products',
    'errors.addProduct': 'Failed to add product',
    'errors.updateProduct': 'Failed to update product',
    'errors.deleteProduct': 'Failed to delete product',
    'errors.fetchProductionSuggestions': 'Failed to fetch production suggestions',
    'errors.DUPLICATE_RAW_MATERIAL_IN_PRODUCT':
      'Raw material is duplicated in the product composition',
    'errors.PRODUCT_CODE_ALREADY_EXISTS': 'Product code already exists',
    'errors.RAW_MATERIAL_CODE_ALREADY_EXISTS': 'Raw material code already exists',
    'errors.PRODUCT_NOT_FOUND': 'Product not found',
    'errors.RAW_MATERIAL_NOT_FOUND': 'Raw material not found',
    'errors.INVALID_FIELD': 'Invalid field',
    'errors.erro': 'Error',
  },
}

export const useI18n = () => {
  const t = (key: keyof TranslationKeys, params?: Record<string, string>) => {
    let text = translations[currentLang.value][key]
    if (params) {
      Object.keys(params).forEach((k) => {
        text = text.replace(`{{ ${k} }}`, params[k] || '')
      })
    }
    return text
  }

  const measurementKeyMap: Record<
    'UNIT' | 'WEIGHT',
    {
      singular: keyof TranslationKeys
      plural: keyof TranslationKeys
    }
  > = {
    UNIT: {
      singular: 'common.unit.singular',
      plural: 'common.unit.plural',
    },
    WEIGHT: {
      singular: 'common.weight.singular',
      plural: 'common.weight.plural',
    },
  }

  const translateMeasurement = (type: string, quantity: number) => {
    if (type in measurementKeyMap) {
      const key =
        quantity === 1
          ? measurementKeyMap[type as keyof typeof measurementKeyMap].singular
          : measurementKeyMap[type as keyof typeof measurementKeyMap].plural

      return t(key)
    }

    return type
  }

  const hasTranslation = (key: string): key is keyof TranslationKeys => {
    return key in translations[currentLang.value]
  }

  const translateOrFallback = (
    message: string,
    fallbackKey?: string,
    errorCode?: string,
    params?: Record<string, string>,
  ) => {
    if (errorCode) {
      const errorKey = `errors.${errorCode}` as keyof TranslationKeys
      if (hasTranslation(errorKey)) {
        return t(errorKey, params)
      }
    }

    if (hasTranslation(message)) {
      return t(message, params)
    }

    if (fallbackKey && hasTranslation(fallbackKey)) {
      return t(fallbackKey, params)
    }

    return message
  }

  const setLang = (lang: Lang) => {
    currentLang.value = lang
  }

  return { t, currentLang, setLang, translateOrFallback, translateMeasurement }
}
