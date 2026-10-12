// The home hero's scroll sequence: the cookie-drop clip plus the eight
// flavours that fan out of the splash. Source of truth for the assets and
// timings is docs/cinematic-cookie-scroll-plan.json and
// docs/cinematic-video-handoff.md.

// Two per range, in final reading order (desktop 4 x 2, mobile 2 x 4).
// The range is the existing catalogue category, shown with its usual chip.
export const COOKIE_LINEUP = [
  { slug: "toasted-almond-vanilla", name: "Toasted Almond & Vanilla", category: "DIABETIC_FRIENDLY" },
  { slug: "cinnamon-pecan", name: "Cinnamon Pecan", category: "DIABETIC_FRIENDLY" },
  { slug: "double-chocolate-chunk", name: "Double Chocolate Chunk", category: "HIGH_PROTEIN" },
  { slug: "vanilla-white-chocolate", name: "Vanilla White Chocolate", category: "HIGH_PROTEIN" },
  { slug: "lemon-almond", name: "Lemon Almond", category: "GLUTEN_FREE" },
  { slug: "coconut-macadamia", name: "Coconut Macadamia", category: "GLUTEN_FREE" },
  { slug: "raspberry-coconut", name: "Raspberry Coconut", category: "VEGAN" },
  { slug: "peanut-chocolate-ripple", name: "Peanut Chocolate Ripple", category: "VEGAN" },
].map((cookie) => ({ ...cookie, image: `/img/cookies/${cookie.slug}.webp` }));

// The generated clip: opaque H.264 with the 80/20 split baked in.
// Pixel positions were measured on the 1920 x 1080 frames.
const VIDEO_DIR = "/video/cookie-drop";
export const COOKIE_DROP_VIDEO = {
  sources: [`${VIDEO_DIR}/cookie-drop-scroll.mp4`, `${VIDEO_DIR}/cookie-drop-web.mp4`],
  poster: `${VIDEO_DIR}/first-frame.webp`,
  splashPoster: `${VIDEO_DIR}/splash-frame.webp`,
  width: 1920,
  height: 1080,
  fps: 30,
  duration: 6,
  splitX: 1536, // tan / chocolate boundary (80%)
  cookie: { cx: 1531, cy: 251, width: 278 }, // suspended cookie, frame 0
  crown: { x: 1536, y: 520 }, // middle of the milk crown at the reveal
  content: { top: 90, bottom: 975 }, // cookie top to glass base: keep this in view
  times: {
    fallStart: 1.0, // still until here
    impact: 2.7, // cookie is in the milk
    reveal: 3.3, // crown is open: the eight cookies take over
    crownPeak: 3.6,
    stop: 3.9, // later frames rebound upward, never shown
  },
};

// The resting hero artwork (/img/cookieHome.webp, 767 x 900). Fractions of
// the image box locate the cookie itself inside its ribbons and crumbs.
export const HERO_ARTWORK = {
  src: "/img/cookieHome.webp",
  width: 767,
  height: 900,
  cookieX: 0.489,
  cookieY: 0.476,
  cookieWidth: 0.816,
  // Turning it this far lines its bite up with the clip's cookie.
  rotationToClip: -20,
};
