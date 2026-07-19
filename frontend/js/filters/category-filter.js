function getCategoryLabel(category) {
  const labels = {
    all: "todos",
    viajes: "viajes",
    familia: "familia",
    celebraciones: "celebraciones"
  };

  return labels[category] || "todos";
}

function updateActiveCategory(
  categoryLinks,
  selectedCategory
) {
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

export function createCategoryFilter({
  categoryLinks,
  activeFilter,
  recentMemoriesSection,
  getMemories,
  onFilter
}) {
  function filterByCategory(category) {
    const memories = getMemories();

    const filteredMemories =
      category === "all"
        ? memories
        : memories.filter(
            (memory) => memory.category === category
          );

    onFilter(filteredMemories);

    updateActiveCategory(
      categoryLinks,
      category
    );

    activeFilter.textContent =
      `Mostrando ${getCategoryLabel(category)}`;

    recentMemoriesSection.scrollIntoView({
      behavior: "smooth",
      block: "start"
    });
  }

  categoryLinks.forEach((link) => {
    link.addEventListener("click", (event) => {
      event.preventDefault();

      filterByCategory(
        link.dataset.category
      );
    });
  });

  return {
    filterByCategory
  };
}