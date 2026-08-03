const MEMORIES_API_URL = "http://localhost:8080/api/memories";

function buildMemoriesUrl({ page, size, category }) {
  const url = new URL(MEMORIES_API_URL);

  url.searchParams.set("page", page);
  url.searchParams.set("size", size);

  if (category && category !== "all") {
    url.searchParams.set("category", category);
  }

  return url;
}

function isValidPagedResponse(data) {
  return (
    data !== null &&
    typeof data === "object" &&
    Array.isArray(data.content) &&
    Number.isInteger(data.page) &&
    Number.isInteger(data.size) &&
    Number.isInteger(data.totalElements) &&
    Number.isInteger(data.totalPages) &&
    typeof data.hasNext === "boolean"
  );
}

export async function fetchMemories({
  page = 0,
  size = 6,
  category = "all"
} = {}) {
  const response = await fetch(
    buildMemoriesUrl({ page, size, category })
  );

  if (!response.ok) {
    throw new Error(
      `Error al cargar recuerdos desde la API: ${response.status}`
    );
  }

  const pageResponse = await response.json();

  if (!isValidPagedResponse(pageResponse)) {
    throw new Error(
      "La API de recuerdos no devolvió una página válida."
    );
  }

  return pageResponse;
}
