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
  onFilter
}) {
  function selectCategory(
    category,
    { shouldScroll = true } = {}
  ) {
    updateActiveCategory(
      categoryLinks,
      category
    );

    activeFilter.textContent =
      `Mostrando ${getCategoryLabel(category)}`;

    onFilter(category);

    if (shouldScroll) {
      recentMemoriesSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
      });
    }
  }

  categoryLinks.forEach((link) => {
    link.addEventListener("click", (event) => {
      event.preventDefault();

      selectCategory(
        link.dataset.category
      );
    });
  });

  return {
    selectCategory
  };
}
