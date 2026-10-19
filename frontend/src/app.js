import { createApp, createSSRApp } from "vue";
import App from "./App.vue";
import { seedCatalog } from "./composables/useCatalog";

/**
 * Builds the app around a router the caller made (web history in the
 * browser, memory history when prerendering). `hydrate` takes over existing
 * prerendered HTML instead of rendering from scratch; `catalog` seeds the
 * cookie list the page was rendered with.
 */
export function createGfcApp({ router, hydrate = false, catalog = null }) {
  const app = hydrate ? createSSRApp(App) : createApp(App);
  if (catalog) seedCatalog(catalog);
  app.use(router);
  return app;
}
