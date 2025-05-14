export default function formatDateTime(datetime: string): string {
  console.log(datetime)
  if (datetime.includes('T')) {
    const d = new Date(datetime)
    const day = String(d.getDate()).padStart(2, '0')
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const year = d.getFullYear()
    const hours = String(d.getHours()).padStart(2, '0')
    const minutes = String(d.getMinutes()).padStart(2, '0')
    return `${day}/${month}/${year} ${hours}:${minutes}`
  }
  const [datePart, timePart] = datetime.split(' ')
  const [year, month, day] = datePart.split('-')
  return `${day}/${month}/${year} ${timePart}`
}
