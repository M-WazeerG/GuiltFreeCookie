<script setup>
/**
 * ProductListView — the full cookie range.
 * Category and search live in the URL (?category=LOW_SUGAR&q=choc) so the
 * home page can deep-link into a range and filtered views can be shared.
 */
import { ref, computed, watch, onMounted, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ArrowPathIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";

import SearchBar from "../components/ui/SearchBar.vue";
import CategoryFilter from "../components/ui/CategoryFilter.vue";
import CookieCard from "../components/cards/CookieCard.vue";
import BaseButton from "../components/ui/BaseButton.vue";
import { useCatalog } from "../composables/useCatalog";
import {
  COOKIE_CATEGORIES,
  formatCategoryLabel,
  categoryBlurb,
} from "../constants/cookieCategories";

const route = useRoute();
const router = useRouter();
const { cookies: items, loading, loaded, error, load } = useCatalog();

const searchBar = ref(null);
const search = ref(typeof route.query.q === "string" ? route.query.q : "");
const category = ref(
  COOKIE_CATEGORIES.includes(route.query.category) ? route.query.category : "all",
);
const sortBy = ref("featured");

const sortOptions = [
  { value: "featured", label: "Featured" },
  { value: "price-low", label: "Price: low to high" },
  { value: "price-high", label: "Price: high to low" },
  { value: "name", label: "Name: A to Z" },
];

onMounted(async () => {
  load();
  if (route.query.focus === "search") {
    await nextTick();
    searchBar.value?.focus();
  }
});

// URL -> state (e.g. the navbar "Products" link clears the query).
watch(
  () => route.query,
  (q) => {
    const nextCat = COOKIE_CATEGORIES.includes(q.category) ? q.category : "all";
    const nextSearch = typeof q.q === "string" ? q.q : "";
    if (nextCat !== category.value) category.value = nextCat;
    if (nextSearch.trim() !== search.value.trim()) search.value = nextSearch;
  },
);

// State -> URL, without stacking history entries.
watch([search, category], ([q, cat]) => {
  const query = {};
  if (q.trim()) query.q = q.trim();
  if (cat !== "all") query.category = cat;
  router.replace({ query });
});

// Counts only include ranges we actually stock, so the chips never lie.
const counts = computed(() => {
  const result = { all: items.value.length };
  items.value.forEach((c) => {
    result[c.category] = (result[c.category] || 0) + 1;
  });
  return result;
});

const categories = computed(() => [
  { id: "all", label: "All cookies" },
  ...COOKIE_CATEGORIES.filter((id) => counts.value[id] || id === category.value).map((id) => ({
    id,
    label: formatCategoryLabel(id),
  })),
]);

const filtered = computed(() => {
  let result = [...items.value];

  const term = search.value.trim().toLowerCase();
  if (term) {
    result = result.filter((c) =>
      [c.description, c.ingredients, formatCategoryLabel(c.category)]
        .filter(Boolean)
        .some((field) => field.toLowerCase().includes(term)),
    );
  }

  if (category.value !== "all") {
    result = result.filter((c) => c.category === category.value);
  }

  switch (sortBy.value) {
    case "price-low":
      result.sort((a, b) => a.price - b.price);
      break;
    case "price-high":
      result.sort((a, b) => b.price - a.price);
      break;
    case "name":
      result.sort((a, b) => (a.description || "").localeCompare(b.description || ""));
      break;
  }

  return result;
});

const resetFilters = () => {
  search.value = "";
  category.value = "all";
  sortBy.value = "featured";
};

const heading = computed(() =>
  category.value === "all" ? "Our cookies" : formatCategoryLabel(category.value),
);
const intro = computed(() =>
  category.value === "all"
    ? "Chocolatey, sweet and properly indulgent, with less of the stuff you'd rather skip."
    : categoryBlurb(category.value),
);
const resultLabel = computed(() => {
  const n = filtered.value.length;
  return `${n} cookie${n === 1 ? "" : "s"}`;
});
</script>

<template>
  <div>
    <!-- Header band -->
    <section class="relative overflow-hidden bg-tan pt-[calc(var(--nav-h)+4rem)] pb-12 md:pb-14">
      <div class="container-magic grid md:grid-cols-[1.1fr_1fr] items-center gap-8">
        <div>
          <h1 class="font-display text-display">{{ heading }}</h1>
          <p class="mt-4 max-w-lg text-base text-chocolate/85">{{ intro }}</p>
        </div>

        <!-- In the flow (not offset) so the whole assortment stays in view -->
        <div class="pointer-events-none hidden md:flex justify-end" aria-hidden="true">
          <img
            src="/img/banners/products-banner.webp"
            alt=""
            width="1400"
            height="861"
            class="w-full max-w-[48rem] max-h-[26rem] object-contain drop-shadow-cookie animate-cookie-in"
          />
        </div>
      </div>
    </section>

    <!-- Toolbar -->
    <div class="sticky top-[var(--nav-h)] z-30 bg-background/90 backdrop-blur-xl border-b border-chocolate/10">
      <div class="container-magic py-3.5 grid gap-3 lg:grid-cols-[1fr_auto] lg:items-center">
        <CategoryFilter v-model="category" :categories="categories" :counts="counts" />

        <div class="flex gap-2.5">
          <SearchBar
            ref="searchBar"
            v-model="search"
            size="sm"
            label="Search cookies"
            placeholder="Search cookies"
            class="lg:w-64"
          />
          <label class="relative shrink-0">
            <span class="sr-only">Sort by</span>
            <select
              v-model="sortBy"
              class="h-11 appearance-none rounded-full border border-cream-500/60 bg-surface pl-4 pr-10 text-sm font-semibold text-chocolate focus:outline-none focus:ring-2 focus:ring-chocolate/20 focus:border-chocolate/50 cursor-pointer"
            >
              <option v-for="opt in sortOptions" :key="opt.value" :value="opt.value">
                {{ opt.label }}
              </option>
            </select>
            <ChevronUpDownIcon class="pointer-events-none absolute right-3.5 top-1/2 -translate-y-1/2 h-5 w-5 text-chocolate/60" />
          </label>
        </div>
      </div>
    </div>

    <div class="container-magic pt-8 pb-24">
      <!-- Loading -->
      <div
        v-if="loading && !loaded"
        class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-x-4 gap-y-10 sm:gap-x-6"
        aria-busy="true"
        aria-label="Loading cookies"
      >
        <div v-for="n in 8" :key="n">
          <div class="shimmer aspect-[4/5] rounded-2xl" />
          <div class="shimmer mt-4 h-4 w-1/3 rounded-full" />
          <div class="shimmer mt-2.5 h-5 w-3/4 rounded-full" />
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="error && !items.length" class="py-24 max-w-md mx-auto text-center" role="alert">
        <img src="/img/cookie4.webp" alt="" class="mx-auto w-28 -rotate-12 opacity-80 drop-shadow-cookie-sm" />
        <h2 class="mt-6 text-base font-bold">The oven's not answering</h2>
        <p class="mt-3 text-chocolate/80">
          We couldn't load the cookies just now. Check your connection and try again.
        </p>
        <BaseButton class="mt-7" @click="load({ force: true })">
          <ArrowPathIcon class="h-5 w-5" />
          Try again
        </BaseButton>
      </div>

      <!-- Nothing stocked at all -->
      <div v-else-if="!items.length" class="py-24 max-w-md mx-auto text-center">
        <img src="/img/cookie2.webp" alt="" class="mx-auto w-28 rotate-12 drop-shadow-cookie-sm" />
        <h2 class="mt-6 text-base font-bold">Fresh batch coming</h2>
        <p class="mt-3 text-chocolate/80">The jar's empty right now. Check back soon.</p>
      </div>

      <!-- No matches -->
      <div v-else-if="filtered.length === 0" class="py-24 max-w-md mx-auto text-center">
        <img src="/img/cookie3.webp" alt="" class="mx-auto w-28 -rotate-6 drop-shadow-cookie-sm" />
        <h2 class="mt-6 text-base font-bold">No crumbs here</h2>
        <p class="mt-3 text-chocolate/80">
          <template v-if="search.trim()">Nothing matches "{{ search.trim() }}"</template>
          <template v-else>Nothing in this range yet</template>.
          Try another search or see the whole range.
        </p>
        <BaseButton class="mt-7" variant="outline" @click="resetFilters">Show all cookies</BaseButton>
      </div>

      <template v-else>
        <h2 class="sr-only">Cookies</h2>
        <p class="mb-6 text-sm font-semibold text-chocolate/70" aria-live="polite">{{ resultLabel }}</p>
        <div class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-x-4 gap-y-10 sm:gap-x-6 sm:gap-y-12">
          <CookieCard
            v-for="(cookie, i) in filtered"
            :key="cookie.cookieId"
            :cookie="cookie"
            :eager="i < 4"
          />
        </div>
      </template>
    </div>
  </div>
</template>
