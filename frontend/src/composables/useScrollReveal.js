/**
 * useScrollReveal — IntersectionObserver-based "reveal on scroll" directive.
 *
 * Usage in a component:
 *   const { vReveal } = useScrollReveal()
 *   <div v-reveal>...</div>
 *
 * Content is visible by default. Only once main.js has added `js-reveal` to
 * <html> do `.reveal` elements start hidden, so a failed script never leaves
 * the page blank. One observer is shared by every component.
 *
 * Pass a numeric delay via :data-reveal-delay="200" (ms) for staggered entrances.
 */
let observer = null;

function getObserver() {
  if (observer || typeof IntersectionObserver === "undefined") return observer;
  observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const el = entry.target;
          el.classList.add("is-visible");
          observer.unobserve(el);
          // Drop the stagger delay once revealed so hover effects aren't delayed.
          if (el.style.transitionDelay) setTimeout(() => (el.style.transitionDelay = ""), 1400);
        }
      });
    },
    { threshold: 0.12, rootMargin: "0px 0px -40px 0px" },
  );
  return observer;
}

const vReveal = {
  mounted(el) {
    el.classList.add("reveal");
    const delay = Number(el.dataset.revealDelay || 0);
    if (delay) el.style.transitionDelay = `${delay}ms`;

    const io = getObserver();
    if (io) io.observe(el);
    else el.classList.add("is-visible");
  },
  unmounted(el) {
    observer?.unobserve(el);
  },
};

export function useScrollReveal() {
  return { vReveal };
}
