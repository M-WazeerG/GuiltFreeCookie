/**
 * The breadcrumb trail for a route. One source for both the visible trail
 * (components/ui/Breadcrumb.vue) and the BreadcrumbList structured data, so
 * the two can never disagree. The last crumb is the current page.
 */
import { COOKIE_CATEGORIES, formatCategoryLabel } from "../constants/cookieCategories";
import { cookieName } from "../composables/useCatalog";

const HOME = { label: "Home", to: { name: "Home" }, path: "/" };
const COOKIES = { label: "Cookies", to: { name: "Products" }, path: "/products" };

const rangeCrumb = (category) => ({
  label: formatCategoryLabel(category),
  to: { name: "Products", query: { category } },
  path: `/products?category=${category}`,
});

export function breadcrumbsFor(route, { cookie = null } = {}) {
  switch (route.name) {
    case "Home":
    case "NotFound":
      return [];
    case "Products": {
      const category = COOKIE_CATEGORIES.includes(route.query?.category) ? route.query.category : null;
      return category ? [HOME, COOKIES, rangeCrumb(category)] : [HOME, COOKIES];
    }
    case "ProductDetail":
      return cookie
        ? [HOME, COOKIES, rangeCrumb(cookie.category), { label: cookieName(cookie), path: route.path }]
        : [HOME, COOKIES];
    default:
      return route.meta?.breadcrumb ? [HOME, { label: route.meta.breadcrumb, path: route.path }] : [];
  }
}
