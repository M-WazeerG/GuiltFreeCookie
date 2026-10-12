import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import ProductListView from "../views/ProductListView.vue";
import ProductDetailView from "../views/ProductDetailView.vue";
import AdminLoginView from "../views/AdminLoginView.vue";
import AdminDashboardView from "../views/AdminDashboardView.vue";
import AdminUsersView from "../views/AdminUsersView.vue";
import AdminOrdersView from "../views/AdminOrdersView.vue";
import AdminProfileView from "../views/AdminProfileView.vue";
import { isAdminLoggedIn } from "../composables/useAdminAuth";

const routes = [
  {
    path: "/",
    name: "Home",
    component: HomeView,
  },
  {
    path: "/products",
    name: "Products",
    component: ProductListView,
    meta: { title: "Our cookies" },
  },
  {
    path: "/product/:id",
    name: "ProductDetail",
    component: ProductDetailView,
  },
  {
    path: "/contact",
    name: "Contact",
    component: () => import("../views/ContactView.vue"),
    meta: { title: "Contact" },
  },
  {
    path: "/about",
    name: "About",
    component: () => import("../views/AboutView.vue"),
    meta: { title: "About" },
  },

  // Legal
  {
    path: "/privacy-policy",
    name: "PrivacyPolicy",
    component: () => import("../views/legal/PrivacyPolicyView.vue"),
    meta: { title: "Privacy Policy" },
  },
  {
    path: "/terms",
    name: "Terms",
    component: () => import("../views/legal/TermsView.vue"),
    meta: { title: "Terms and Conditions" },
  },
  {
    path: "/cookie-policy",
    name: "CookiePolicy",
    component: () => import("../views/legal/CookiePolicyView.vue"),
    meta: { title: "Cookie Policy" },
  },
  {
    path: "/refund-policy",
    name: "RefundPolicy",
    component: () => import("../views/legal/RefundPolicyView.vue"),
    meta: { title: "Refund Policy" },
  },

  // Admin Routes

  {
    path: "/admin",
    name: "AdminLogin",
    component: AdminLoginView,
    meta: { title: "Admin Login", hideChrome: true },
  },
  {
    path: "/admindashboard",
    redirect: "/admindashboard/cookies",
  },
  {
    path: "/admindashboard/cookies",
    name: "AdminDashboard",
    component: AdminDashboardView,
    meta: { title: "Admin Dashboard", requiresAdmin: true },
  },
  {
    path: "/admindashboard/users",
    name: "AdminUsers",
    component: AdminUsersView,
    meta: { title: "Admin Users", requiresAdmin: true },
  },
  {
    path: "/admindashboard/orders",
    name: "AdminOrders",
    component: AdminOrdersView,
    meta: { title: "Admin Orders", requiresAdmin: true },
  },
  {
    path: "/admindashboard/profile",
    name: "AdminProfile",
    component: AdminProfileView,
    meta: { title: "My Profile", requiresAdmin: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition;
    if (to.hash) return { el: to.hash, top: 96, behavior: "smooth" };
    // Query-only changes (product list filters) keep the scroll position.
    if (to.path === from.path) return false;
    return { top: 0 };
  },
});

router.beforeEach((to) => {
  if (to.meta.requiresAdmin && !isAdminLoggedIn()) {
    return { name: "AdminLogin", query: { redirect: to.fullPath } };
  }

  if (to.name === "AdminLogin" && isAdminLoggedIn()) {
    return { name: "AdminDashboard" };
  }
});

const DEFAULT_TITLE = "GuiltFreeCookie | Chocolate cookies that love you back";
router.afterEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} | GuiltFreeCookie` : DEFAULT_TITLE;
});

export default router;
