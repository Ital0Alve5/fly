export default function formatDateTime(datetime: string): string {
  const [datePart, timePart] = datetime.split(' ')
  const [year, month, day] = datePart.split('-')
  return `${day}/${month}/${year} ${timePart}`
}
