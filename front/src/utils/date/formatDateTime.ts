export function formatDateTime(datetime: string): string {
  const date = new Date(datetime)

  if (isNaN(date.getTime())) {
    return 'Data inválida'
  }

  return date.toLocaleString('pt-BR', {
    timeZone: 'America/Sao_Paulo',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  })
}
