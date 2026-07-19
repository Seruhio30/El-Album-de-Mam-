import { renderMemories } from "./components/memory-card.js";
import { fetchMemories } from "./data/memories-service.js";
import { createCategoryFilter } from "./filters/category-filter.js";
import { formatDate } from "./utils/date.js";
import { createPhotoViewer } from "./viewers/photo-viewer.js";
import { createVideoViewer } from "./viewers/video-viewer.js";


const memoriesGrid = document.querySelector("#memories-grid");

const categoryLinks = document.querySelectorAll(
  ".category-card[data-category]"
);

const activeFilter = document.querySelector("#active-filter");

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

const photoViewerController = createPhotoViewer({
  viewer: photoViewer,
  backButton: photoViewerBack,
  image: photoViewerImage,
  title: photoViewerTitle,
  details: photoViewerDetails,
  description: photoViewerDescription,
  hideHomeSections,
  showHomeSections,
  recentMemoriesSection
});

const videoViewer = document.querySelector("#video-viewer");
const videoViewerBack = document.querySelector("#video-viewer-back");
const videoViewerPlayer = document.querySelector("#video-viewer-player");
const videoViewerTitle = document.querySelector("#video-viewer-title");
const videoViewerDetails = document.querySelector("#video-viewer-details");
const videoViewerDescription = document.querySelector(
  "#video-viewer-description"
);

const videoViewerController = createVideoViewer({
  viewer: videoViewer,
  backButton: videoViewerBack,
  player: videoViewerPlayer,
  title: videoViewerTitle,
  details: videoViewerDetails,
  description: videoViewerDescription,
  hideHomeSections,
  showHomeSections,
  recentMemoriesSection
});



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


function displayMemories(memories) {
  renderMemories(
    memoriesGrid,
    memories,
    {
      onPhotoClick: photoViewerController.open,
     onVideoClick: videoViewerController.open
    }
  );
}

const categoryFilterController = createCategoryFilter({
  categoryLinks,
  activeFilter,
  recentMemoriesSection,
  getMemories: () => allMemories,
  onFilter: displayMemories
});

function showLoadError() {
  memoriesGrid.innerHTML = `
    <p class="memories-grid__status memories-grid__status--error">
      No fue posible cargar los recuerdos.
    </p>
  `;
}

async function loadMemories() {
  try {
    allMemories = await fetchMemories()
   categoryFilterController.filterByCategory("all");
  } catch (error) {
    console.error(error);
    showLoadError();
  }
}

loadMemories();