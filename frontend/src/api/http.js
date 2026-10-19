// Where the Spring Boot API lives. In development Vite proxies this path to
// localhost:8080 (see vite.config.js); in production either serve the API
// under the same path on the site's domain, or set VITE_API_BASE_URL to its
// full URL, e.g. https://api.guiltfreecookie.co.za/guiltFreeCookie.
export const API_BASE = (import.meta.env.VITE_API_BASE_URL || "/guiltFreeCookie").replace(/\/+$/, "");
