import { formatDate } from "../utils/date.js";

export function createPhotoViewer({
  viewer,
  backButton,
  image,
  title,
  details,
  description,
  hideHomeSections,
  showHomeSections,
  recentMemoriesSection
}) {
  function open(memory) {
    hideHomeSections();

    image.src = memory.file;
    image.alt = memory.description || memory.title;

    title.textContent = memory.title;
    details.textContent =
      `${memory.place} · ${formatDate(memory.date)}`;

    description.textContent =
      memory.description || "Recuerdo familiar.";

    viewer.hidden = false;
    backButton.focus();

    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  }

  function close() {
    viewer.hidden = true;

    image.src = "";
    image.alt = "";

    showHomeSections();

    recentMemoriesSection.scrollIntoView({
      behavior: "smooth"
    });
  }

  backButton.addEventListener("click", close);

  return {
    open,
    close
  };
}