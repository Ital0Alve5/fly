export const currencyMask = {
  mask: 'R$ 0,99',
  tokens: {
    '0': { pattern: /\d/, optional: true, multiple: true },
    '9': { pattern: /\d/, optional: false },
  },
}
