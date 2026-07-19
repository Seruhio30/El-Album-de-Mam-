import { formatDate } from "../utils/date.js";

function getMemoryTypeLabel(type) {
  return type === "video" ? "Video" : "Fotografía";
}

function getMemoryActionLabel(type) {
  return type === "video"
    ? "Reproducir video"
    : "Ver recuerdo";
}

function createMemoryPlaceholder(type) {
  const placeholder = document.createElement("div");
  placeholder.className = "memory-card__placeholder";

  const icon = document.createElement("span");
  icon.setAttribute("aria-hidden", "true");
  icon.textContent = type === "video" ? "▶️" : "📷";

  placeholder.appendChild(icon);

  return placeholder;
}

function createMemoryImage(memory) {
  const imageContainer = document.createElement("div");
  imageContainer.className = "memory-card__media";

  const image = document.createElement("img");
  image.className = "memory-card__image";
  image.src = memory.thumbnail;
  image.alt = memory.description || memory.title;
  image.loading = "lazy";

  image.addEventListener("error", () => {
    imageContainer.replaceWith(
      createMemoryPlaceholder(memory.type)
    );
  });

  imageContainer.appendChild(image);

  if (memory.type === "video") {
    const videoIndicator = document.createElement("span");
    videoIndicator.className = "memory-card__video-indicator";
    videoIndicator.setAttribute("aria-hidden", "true");
    videoIndicator.textContent = "▶";

    imageContainer.appendChild(videoIndicator);
  }

  return imageContainer;
}

function createMemoryCard(
  memory,
  { onPhotoClick, onVideoClick }
) {
  const article = document.createElement("article");
  article.className = "memory-card";
  article.dataset.category = memory.category;
  article.dataset.type = memory.type;

  const content = document.createElement("div");
  content.className = "memory-card__content";

  const type = document.createElement("p");
  type.className = "memory-card__type";
  type.textContent = getMemoryTypeLabel(memory.type);

  const title = document.createElement("h3");
  title.className = "memory-card__title";
  title.textContent = memory.title;

  const details = document.createElement("p");
  details.className = "memory-card__details";
  details.textContent =
    `${memory.place} · ${formatDate(memory.date)}`;

  const link = document.createElement("a");
  link.className = "memory-card__link";
  link.href = memory.file;
  link.textContent = getMemoryActionLabel(memory.type);

  if (memory.type === "photo") {
    link.setAttribute(
      "aria-label",
      `Ver fotografía: ${memory.title}`
    );

    link.addEventListener("click", (event) => {
      event.preventDefault();
      onPhotoClick(memory);
    });
  }

  if (memory.type === "video") {
    link.setAttribute(
      "aria-label",
      `Reproducir video: ${memory.title}`
    );

    link.addEventListener("click", (event) => {
      event.preventDefault();
      onVideoClick(memory);
    });
  }

  content.append(type, title, details, link);
  article.append(createMemoryImage(memory), content);

  return article;
}

export function renderMemories(
  memoriesGrid,
  memories,
  handlers
) {
  memoriesGrid.innerHTML = "";

  if (memories.length === 0) {
    memoriesGrid.innerHTML = `
      <p class="memories-grid__status">
        Todavía no hay recuerdos disponibles.
      </p>
    `;

    return;
  }

  const fragment = document.createDocumentFragment();

  memories.forEach((memory) => {
    const memoryCard = createMemoryCard(
      memory,
      handlers
    );

    fragment.appendChild(memoryCard);
  });

  memoriesGrid.appendChild(fragment);
}