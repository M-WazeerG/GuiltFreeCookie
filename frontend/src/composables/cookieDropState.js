import { ref } from "vue";

// True while the home hero's cookie-drop sequence is pinned on screen: the
// navbar stays transparent over it, as it does over the resting hero. Kept
// apart from useCookieDropScroll so reading it doesn't load GSAP.
export const cookieDropPinned = ref(false);
