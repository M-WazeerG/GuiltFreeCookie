/**
 * useFormat — small formatting helpers used across the app.
 */
export function useFormat() {
  const currency = (value, locale = 'en-US', currency = 'USD') =>
    new Intl.NumberFormat(locale, {
      style: 'currency',
      currency,
    }).format(Number(value || 0))

  // Rand prices as shown across the store: "R35.00"
  const rand = (value) => `R${Number(value || 0).toFixed(2)}`

  const date = (value, locale = 'en-US') =>
    new Intl.DateTimeFormat(locale, {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    }).format(new Date(value))

  const truncate = (str, n = 90) =>
    !str ? '' : str.length > n ? `${str.slice(0, n).trimEnd()}…` : str

  return { currency, rand, date, truncate }
}
