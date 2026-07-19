const memoriesGrid = document.querySelector("#memories-grid");
const welcomeSection = document.querySelector(".welcome");
const categoriesSection = document.querySelector(".categories");
const recentMemoriesSection = document.querySelector("#recent-memories");

const photoViewer = document.querySelector("#photo-viewer");
const photoViewerBack = document.querySelector("#photo-viewer-back");
const photoViewerImage = document.querySelector("#photo-viewer-image");
const photoViewerTitle = document.querySelector("#photo-viewer-title");
const photoViewerDetails = document.querySelector("#photo-viewer-details");
const photoViewerDescription = document.querySelector(
  "#photo-viewer-description"
);

const videoViewer = document.querySelector("#video-viewer");
const videoViewerBack = document.querySelector("#video-viewer-back");
const videoViewerPlayer = document.querySelector("#video-viewer-player");
const videoViewerTitle = document.querySelector("#video-viewer-title");
const videoViewerDetails = document.querySelector("#video-viewer-details");
const videoViewerDescription = document.querySelector(
  "#video-viewer-description"
);

function formatDate(dateValue) {
  const date = new Date(`${dateValue}T00:00:00`);

  return new Intl.DateTimeFormat("es-CR", {
    month: "long",
    year: "numeric"
  }).format(date);
}

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

function hideHomeSections() {
  welcomeSection.hidden = true;
  categoriesSection.hidden = true;
  recentMemoriesSection.hidden = true;
}

function showHomeSections() {
  welcomeSection.hidden = false;
  categoriesSection.hidden = false;
  recentMemoriesSection.hidden = false;
}

function openPhotoViewer(memory) {
  hideHomeSections();

  photoViewerImage.src = memory.file;
  photoViewerImage.alt = memory.description || memory.title;

  photoViewerTitle.textContent = memory.title;
  photoViewerDetails.textContent =
    `${memory.place} · ${formatDate(memory.date)}`;

  photoViewerDescription.textContent =
    memory.description || "Recuerdo familiar.";

  photoViewer.hidden = false;

  photoViewerBack.focus();

  window.scrollTo({
    top: 0,
    behavior: "smooth"
  });
}

function closePhotoViewer() {
  photoViewer.hidden = true;

  photoViewerImage.src = "";
  photoViewerImage.alt = "";

  showHomeSections();

  recentMemoriesSection.scrollIntoView({
    behavior: "smooth"
  });
}

function openVideoViewer(memory) {
  hideHomeSections();

  videoViewerPlayer.src = memory.file;
  videoViewerTitle.textContent = memory.title;
  videoViewerDetails.textContent =
    `${memory.place} · ${formatDate(memory.date)}`;

  videoViewerDescription.textContent =
    memory.description || "Recuerdo familiar.";

  videoViewer.hidden = false;

  videoViewerBack.focus();

  window.scrollTo({
    top: 0,
    behavior: "smooth"
  });
}

function closeVideoViewer() {
  videoViewerPlayer.pause();
  videoViewerPlayer.removeAttribute("src");
  videoViewerPlayer.load();

  videoViewer.hidden = true;

  showHomeSections();

  recentMemoriesSection.scrollIntoView({
    behavior: "smooth"
  });
}

function createMemoryCard(memory) {
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
  details.textContent = `${memory.place} · ${formatDate(memory.date)}`;

  const link = document.createElement("a");
  link.className = "memory-card__link";
  link.href = memory.file;
  link.textContent = getMemoryActionLabel(memory.type);

  if (memory.type === "video") {
    link.setAttribute(
      "aria-label",
      `Reproducir video: ${memory.title}`
    );
  } else {
    link.setAttribute(
      "aria-label",
      `Ver fotografía: ${memory.title}`
    );
  }
  if (memory.type === "photo") {
    link.addEventListener("click", (event) => {
      event.preventDefault();
      openPhotoViewer(memory);
    });
  }

  if (memory.type === "video") {
    link.addEventListener("click", (event) => {
      event.preventDefault();
      openVideoViewer(memory);
    });
  }

  content.append(type, title, details, link);
  article.append(createMemoryImage(memory), content);

  return article;
}

function renderMemories(memories) {
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
    fragment.appendChild(createMemoryCard(memory));
  });

  memoriesGrid.appendChild(fragment);
}

function showLoadError() {
  memoriesGrid.innerHTML = `
    <p class="memories-grid__status memories-grid__status--error">
      No fue posible cargar los recuerdos.
    </p>
  `;
}

async function loadMemories() {
  try {
    const response = await fetch("data/memories.json");

    if (!response.ok) {
      throw new Error(
        `Error al cargar recuerdos: ${response.status}`
      );
    }

    const memories = await response.json();

    renderMemories(memories);
  } catch (error) {
    console.error(error);
    showLoadError();
  }
}

photoViewerBack.addEventListener("click", closePhotoViewer);
videoViewerBack.addEventListener("click", closeVideoViewer);
loadMemories();