export function extractPriceValue(price: string): number {
  if (!price) {
    return 0
  }

  const withoutCurrency = price.replace('R$', '').trim()
  const noThousandSeparator = withoutCurrency.replace(/\./g, '')
  const standardized = noThousandSeparator.replace(',', '.')

  return parseFloat(standardized)
}
