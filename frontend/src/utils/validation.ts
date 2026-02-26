export const validateCode = (code: string): boolean => {
  const regex = /^[A-Z0-9]+$/
  return regex.test(code)
}

export const validateQuantity = (quantity: number): boolean => {
  const regex = /^\d{1,17}(\.\d{1,2})?$/
  return regex.test(quantity.toString())
}
