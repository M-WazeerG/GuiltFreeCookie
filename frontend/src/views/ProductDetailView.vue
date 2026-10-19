<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { ShoppingBagIcon } from "@heroicons/vue/20/solid";

import BaseButton from "../components/ui/BaseButton.vue";
import Breadcrumb from "../components/ui/Breadcrumb.vue";
import CookieInfoCard from "../components/cookieDetails/CookieInfoCard.vue";
import RelatedCookieCard from "../components/cookieDetails/RelatedCookieCard.vue";
import QuantitySelector from "../components/cookieDetails/QuantitySelector.vue";
import { useCatalog, cookieImage, cookieName, imageFallback } from "../composables/useCatalog";
import { useFormat } from "../composables/useFormat";
import { useToast } from "../composables/useToast";
import { formatCategoryLabel, categoryBlurb } from "../constants/cookieCategories";
import { breadcrumbsFor } from "../seo/breadcrumbs";
import { applyHead, findCookie, headForRoute } from "../seo/head";

const route = useRoute();
const { rand } = useFormat();
const { showToast } = useToast();
const loadFailed = ref(false);
const { cookies: catalog, error: catalogError, load: loadCatalog } = useCatalog();

// Start from the catalogue when it has this cookie (always, on a
// prerendered page), so the first render is the finished page.
const cookie = ref(findCookie(catalog.value, route.params.id));
const loading = ref(!cookie.value);
const quantity = ref(1);

const image = computed(() => cookieImage(cookie.value));
const name = computed(() => cookieName(cookie.value));
const categoryLabel = computed(() => formatCategoryLabel(cookie.value?.category));
// The cookie's own copy if it has some, otherwise the range's one-liner.
const blurb = computed(() => cookie.value?.longDescription?.trim() || categoryBlurb(cookie.value?.category));
const total = computed(() => (cookie.value?.price || 0) * quantity.value);
const crumbs = computed(() => breadcrumbsFor(route, { cookie: cookie.value }));

// Same range first, then the rest of the jar.
const related = computed(() => {
  if (!cookie.value) return [];
  const others = catalog.value.filter((c) => c.cookieId !== cookie.value.cookieId);
  const same = others.filter((c) => c.category === cookie.value.category);
  const rest = others.filter((c) => c.category !== cookie.value.category);
  return [...same, ...rest].slice(0, 4);
});

// Hooks for the cart feature to wire up. Until then, say so instead of
// silently doing nothing.
const addToCart = () => {
  showToast("The cart is almost ready. You'll be able to order very soon.", "info");
};
const buyNow = () => {
  addToCart();
};

// The product comes from the same catalogue as the rest of the site (one
// shared request, already loaded when arriving from the product list), so
// a cookie that's gone or archived is simply "not found". "Couldn't reach
// the server" is different: only that one is worth retrying, and a cookie
// already on screen stays there if the refresh fails.
const fetchCookie = async ({ retry = false } = {}) => {
  if (!cookie.value) loading.value = true;
  loadFailed.value = false;
  const list = await loadCatalog({ force: retry });
  loading.value = false;

  if (catalogError.value) {
    loadFailed.value = !cookie.value;
    return;
  }
  cookie.value = findCookie(list, route.params.id);
  applyHead(headForRoute(route, { cookie: cookie.value, missing: !cookie.value }));
};

onMounted(() => fetchCookie());
</script>

<template>
  <div class="pt-[var(--nav-h)]">
    <!-- Loading -->
    <div v-if="loading" class="container-magic pt-8 pb-24" aria-busy="true" aria-label="Loading cookie">
      <div class="shimmer h-4 w-56 rounded-full" />
      <div class="mt-6 grid lg:grid-cols-[1.05fr_0.95fr] gap-10 lg:gap-16">
        <div class="shimmer aspect-square rounded-[28px]" />
        <div class="space-y-5 pt-4">
          <div class="shimmer h-5 w-28 rounded-full" />
          <div class="shimmer h-16 w-4/5 rounded-2xl" />
          <div class="shimmer h-8 w-32 rounded-full" />
          <div class="shimmer h-14 w-full rounded-full mt-10" />
          <div class="shimmer h-40 w-full rounded-2xl mt-8" />
        </div>
      </div>
    </div>

    <!-- Couldn't reach the server -->
    <div v-else-if="loadFailed" class="container-magic py-28 max-w-lg text-center" role="alert">
      <img src="/img/cookies/lemon-almond.webp" alt="" class="mx-auto w-32 rotate-6 drop-shadow-cookie-sm" />
      <h1 class="mt-6 text-base font-bold">We couldn't load this cookie</h1>
      <p class="mt-2 text-base text-chocolate/80">
        Check your connection, then try again.
      </p>
      <BaseButton class="mt-8" @click="fetchCookie({ retry: true })">Try again</BaseButton>
    </div>

    <!-- Not found -->
    <div v-else-if="!cookie" class="container-magic py-28 max-w-lg text-center">
      <img src="/img/cookies/cinnamon-pecan.webp" alt="" class="mx-auto w-32 -rotate-12 drop-shadow-cookie-sm" />
      <h1 class="font-display mt-6 text-display">This one's been eaten</h1>
      <p class="mt-3 text-base text-chocolate/80">
        We couldn't find that cookie. It may have sold out or moved.
      </p>
      <BaseButton :to="{ name: 'Products' }" class="mt-8">See all cookies</BaseButton>
    </div>

    <template v-else>
      <div class="container-magic pt-6 pb-20 md:pb-28">
        <Breadcrumb :items="crumbs" class="mb-6" />

        <div class="grid lg:grid-cols-[1.05fr_0.95fr] gap-10 lg:gap-16 items-start">
          <!-- Media -->
          <div class="lg:sticky lg:top-[calc(var(--nav-h)+2.4rem)]">
            <div
              class="relative isolate aspect-square overflow-hidden rounded-[28px] bg-chocolate flex items-center justify-center"
            >
              <div
                class="absolute inset-0 -z-10 bg-[radial-gradient(50%_45%_at_50%_52%,rgba(197,159,113,0.4),transparent_72%)]"
                aria-hidden="true"
              />
              <img
                :src="image"
                @error="imageFallback(cookie)($event)"
                :alt="name"
                fetchpriority="high"
                class="w-[78%] h-[78%] object-contain drop-shadow-[0_40px_40px_rgba(0,0,0,0.45)] animate-cookie-in"
              />
            </div>
          </div>

          <!-- Details -->
          <div class="lg:pt-2">
            <RouterLink
              :to="{ name: 'Products', query: { category: cookie.category } }"
              class="inline-flex items-center min-h-11 rounded-full bg-tan px-4 py-1.5 text-sm font-semibold text-chocolate hover:bg-tan-dark transition-colors"
            >
              {{ categoryLabel }}
            </RouterLink>

            <h1 class="font-display mt-4 text-display">{{ name }}</h1>
            <p v-if="blurb" class="mt-3 text-base text-chocolate/80">{{ blurb }}</p>

            <p class="mt-5 text-base font-bold tabular">{{ rand(cookie.price) }}</p>

            <!-- Purchase -->
            <div class="mt-8 flex flex-wrap items-center gap-3">
              <QuantitySelector v-model="quantity" />
              <BaseButton size="lg" class="h-14 flex-1 min-w-[19.2rem]" @click="addToCart">
                <ShoppingBagIcon class="h-5 w-5" />
                Add to cart
                <span class="font-normal text-cream-100/75 tabular" v-if="quantity > 1">&middot; {{ rand(total) }}</span>
              </BaseButton>
            </div>
            <BaseButton variant="outline" size="lg" block class="mt-3 h-14" @click="buyNow">
              Buy now
            </BaseButton>

            <!-- Details -->
            <CookieInfoCard :cookie="cookie" class="mt-8" />
          </div>
        </div>
      </div>

      <!-- Related -->
      <section v-if="related.length" class="bg-background py-20 md:py-24" aria-labelledby="related-title">
        <div class="container-magic">
          <div class="flex flex-wrap items-end justify-between gap-4 mb-10">
            <h2 id="related-title" class="font-display text-display">You might also like</h2>
            <RouterLink
              :to="{ name: 'Products' }"
              class="inline-flex items-center min-h-11 font-semibold underline decoration-primary decoration-2 underline-offset-[6px] hover:decoration-chocolate"
            >
              See all cookies
            </RouterLink>
          </div>
          <div class="grid md:grid-cols-2 gap-4 sm:gap-5">
            <RelatedCookieCard v-for="c in related" :key="c.cookieId" :related="c" />
          </div>
        </div>
      </section>
    </template>
  </div>
</template>
