export function formatDate(dateValue) {
  const date = new Date(`${dateValue}T00:00:00`);

  return new Intl.DateTimeFormat("es-CR", {
    month: "long",
    year: "numeric"
  }).format(date);
}