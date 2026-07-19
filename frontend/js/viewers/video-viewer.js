import { formatDate } from "../utils/date.js";

export function createVideoViewer({
  viewer,
  backButton,
  player,
  title,
  details,
  description,
  hideHomeSections,
  showHomeSections,
  recentMemoriesSection
}) {
  function open(memory) {
    hideHomeSections();

    player.src = memory.file;

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
    player.pause();
    player.removeAttribute("src");
    player.load();

    viewer.hidden = true;

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