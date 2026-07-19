import { formatDate } from "./utils/date.js";
import { fetchMemories } from "./data/memories-service.js";
import { renderMemories } from "./components/memory-card.js";


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

const categoryLinks = document.querySelectorAll(
  ".category-card[data-category]"
);

const activeFilter = document.querySelector("#active-filter");

let allMemories = [];



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

function displayMemories(memories) {
  renderMemories(
    memoriesGrid,
    memories,
    {
      onPhotoClick: openPhotoViewer,
      onVideoClick: openVideoViewer
    }
  );
}

function getCategoryLabel(category) {
  const labels = {
    all: "todos",
    viajes: "viajes",
    familia: "familia",
    celebraciones: "celebraciones"
  };

  return labels[category] || "todos";
}


function updateActiveCategory(selectedCategory) {
  categoryLinks.forEach((link) => {
    const isActive =
      link.dataset.category === selectedCategory;

    link.classList.toggle(
      "category-card--active",
      isActive
    );

    if (isActive) {
      link.setAttribute("aria-current", "true");
    } else {
      link.removeAttribute("aria-current");
    }
  });
}

function filterMemoriesByCategory(category) {
  const filteredMemories =
    category === "all"
      ? allMemories
      : allMemories.filter(
        (memory) => memory.category === category
      );

 displayMemories(filteredMemories);
  updateActiveCategory(category);

  activeFilter.textContent =
    `Mostrando ${getCategoryLabel(category)}`;

  recentMemoriesSection.scrollIntoView({
    behavior: "smooth",
    block: "start"
  });
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
    allMemories = await fetchMemories();

   
    displayMemories(allMemories);
    updateActiveCategory("all");
  } catch (error) {
    console.error(error);
    showLoadError();
  }
}

categoryLinks.forEach((link) => {
  link.addEventListener("click", (event) => {
    event.preventDefault();

    const selectedCategory = link.dataset.category;

    filterMemoriesByCategory(selectedCategory);
  });
});

photoViewerBack.addEventListener(
  "click",
  closePhotoViewer
);

videoViewerBack.addEventListener(
  "click",
  closeVideoViewer
);

loadMemories();