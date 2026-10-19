/**
 * Build-time entry: renders one URL to HTML for scripts/prerender.mjs.
 * Never shipped to the browser.
 */
import { renderToString } from "vue/server-renderer";
import { createMemoryHistory } from "vue-router";
import { createGfcApp } from "./app";
import { createAppRouter } from "./router";
import { headForRoute, renderHead, safeJson } from "./seo/head";

export { indexablePaths, sitemapXml, robotsTxt, llmsTxt } from "./seo/files";
export { SITE_URL } from "./seo/site";

export async function render(url, { catalog = null } = {}) {
  const router = createAppRouter(createMemoryHistory());
  const app = createGfcApp({ router, hydrate: true, catalog });

  await router.push(url);
  await router.isReady();

  const ctx = {};
  const html = await renderToString(app, ctx);
  const route = router.currentRoute.value;

  return {
    html,
    teleports: ctx.teleports?.["#teleports"] ?? "",
    modules: ctx.modules ?? new Set(),
    head: renderHead(headForRoute(route, { catalog: catalog || [] })),
    catalogJson: catalog ? safeJson(catalog) : null,
    route: { name: String(route.name), path: route.path },
  };
}
