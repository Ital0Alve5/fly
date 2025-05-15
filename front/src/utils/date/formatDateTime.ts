export function formatDateTime(datetime: string): string {
  if (datetime.includes('Z')) {
    const [datePart, timePart] = datetime.split('T');
    const [year, month, day] = datePart.split('-');
    const [time] = timePart.split('Z');
    const [hours, minutes] = time.split(':');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }

  if (datetime.includes('T')) {
    const [datePart, timePart] = datetime.split('T');
    const [year, month, day] = datePart.split('-');
    const [hours, minutes] = timePart.split(':');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }

  const [datePart, timePart] = datetime.split(' ');
  const [year, month, day] = datePart.split('-');
  return `${day}/${month}/${year} ${timePart}`;
}
