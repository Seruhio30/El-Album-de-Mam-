const MEMORIES_URL = "data/memories.json";

export async function fetchMemories() {
  const response = await fetch(MEMORIES_URL);

  if (!response.ok) {
    throw new Error(
      `Error al cargar recuerdos: ${response.status}`
    );
  }

  const memories = await response.json();

  if (!Array.isArray(memories)) {
    throw new Error(
      "El archivo de recuerdos no contiene una lista válida."
    );
  }

  return memories;
}