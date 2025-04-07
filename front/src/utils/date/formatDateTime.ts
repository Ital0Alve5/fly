export default function formatDateTime(datetime: string): string {
  if (datetime.includes('T')) {
    const [datePart, timePart] = datetime.split('T')
    const [year, month, day] = datePart.split('-')
    const [hours, minutes] = timePart.split(':')
    return `${day}/${month}/${year} ${hours}:${minutes}`
  }

  const [datePart, timePart] = datetime.split(' ')
  const [year, month, day] = datePart.split('-')
  return `${day}/${month}/${year} ${timePart}`
}
