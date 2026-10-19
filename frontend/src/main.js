import axios from "axios";
import { createWebHistory } from "vue-router";
import { createGfcApp } from "./app";
import { createAppRouter } from "./router";
import { getAdminToken, useAdminAuth } from "./composables/useAdminAuth";
import { useCatalog } from "./composables/useCatalog";
import { applyHead, headForRoute } from "./seo/head";
import "./assets/main.css";

const router = createAppRouter(createWebHistory());

// Attach the admin's bearer token to every outgoing request. Harmless on
// public endpoints (the backend just ignores it there) and required for
// anything behind SecurityConfig's authorizeHttpRequests rules.
axios.interceptors.request.use((config) => {
  const token = getAdminToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// A 401 while we believed we had a session means the token expired or was
// rejected server-side — clear the stale session and send the admin back
// to log in. Guarded on getAdminToken() so a plain wrong-password response
// from the login form itself isn't mistaken for a session expiring, and
// changePassword is excluded because it also answers 401 for "your current
// password was wrong" — a form validation error, not an expired session —
// which AdminProfileView already handles inline.
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    const isChangePasswordCall = error.config?.url?.includes("/changePassword/");
    if (error.response?.status === 401 && getAdminToken() && !isChangePasswordCall) {
      useAdminAuth().logout();
      router.push({ name: "AdminLogin" });
    }
    return Promise.reject(error);
  },
);

const { cookies } = useCatalog();
router.afterEach((to) => applyHead(headForRoute(to, { catalog: cookies.value })));

// Opt in to scroll-reveal hiding only once JS is running (see main.css).
document.documentElement.classList.add("js-reveal");

const root = document.getElementById("app");
const catalog = readJson("catalog-data");

// Prerendered pages are hydrated: Vue adopts the HTML that's already on
// screen. A page served for a different URL (404.html standing in for a
// product added after the build, or /products?category=… which was
// prerendered unfiltered) is rendered fresh instead.
const current = router.resolve(window.location.pathname + window.location.search + window.location.hash);
const hydrate = canHydrate(root, current);
if (!hydrate) {
  document.getElementById("teleports")?.replaceChildren();
}

const app = createGfcApp({ router, hydrate, catalog });
router.isReady().then(() => app.mount(root));

function canHydrate(el, route) {
  const ssrPath = el?.dataset.ssrPath;
  if (!ssrPath) return false;
  if (route.name === "NotFound" || el.dataset.ssrRoute === "NotFound") {
    return route.name === el.dataset.ssrRoute;
  }
  const rendersQuery = (route.meta.renderQuery || []).some((key) => route.query[key] != null);
  const trim = (path) => path.replace(/(.)\/+$/, "$1");
  return !rendersQuery && trim(route.path) === trim(ssrPath);
}

function readJson(id) {
  const el = document.getElementById(id);
  if (!el) return null;
  try {
    return JSON.parse(el.textContent);
  } catch {
    return null;
  }
}
