export function getTodayDate(): string {
  const now = new Date();
  const formattedDate = now.toLocaleDateString('pt-BR') + ' ' + now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0') + ':' + now.getSeconds().toString().padStart(2, '0');
  return formattedDate;
}
