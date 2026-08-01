const MEMORIES_API_URL = "http://localhost:8080/api/memories";

export async function fetchMemories() {
  const response = await fetch(MEMORIES_API_URL);

  if (!response.ok) {
    throw new Error(
      `Error al cargar recuerdos desde la API: ${response.status}`
    );
  }

  const memories = await response.json();

  if (!Array.isArray(memories)) {
    throw new Error(
      "La API de recuerdos no devolvió una lista válida."
    );
  }

  return memories;
}
