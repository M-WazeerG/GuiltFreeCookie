<template>
  <div class="rounded-2xl bg-surface shadow-soft divide-y divide-chocolate/10">
    <!-- Allergens first: it's the thing people need to check -->
    <div class="flex gap-3.5 p-5 sm:p-6">
      <span
        :class="[
          'h-10 w-10 shrink-0 rounded-full flex items-center justify-center',
          allergens ? 'bg-primary/30 text-primary-800' : 'bg-chocolate/[0.07] text-chocolate/70',
        ]"
      >
        <ExclamationTriangleIcon v-if="allergens" class="h-5 w-5" />
        <CheckBadgeIcon v-else class="h-5 w-5" />
      </span>
      <div>
        <h2 class="text-base">Allergens</h2>
        <p class="mt-0.5 text-chocolate/80">
          <template v-if="allergens">{{ allergens }}.</template>
          <template v-else>None listed. Check the ingredients if you have a specific intolerance.</template>
        </p>
      </div>
    </div>

    <!-- Ingredients -->
    <div v-if="ingredients.length" class="p-5 sm:p-6">
      <h2 class="text-base">Ingredients</h2>
      <ul class="mt-3 flex flex-wrap gap-2">
        <li
          v-for="item in ingredients"
          :key="item"
          class="rounded-full bg-chocolate/[0.07] px-3.5 py-1.5 text-sm text-chocolate"
        >
          {{ item }}
        </li>
      </ul>
    </div>

    <!-- Range -->
    <div class="flex flex-wrap items-center justify-between gap-3 p-5 sm:p-6">
      <div>
        <h2 class="text-base">{{ categoryLabel }}</h2>
        <p class="mt-0.5 text-chocolate/80">{{ blurb }}</p>
      </div>
      <RouterLink
        :to="{ name: 'Products', query: { category: cookie.category } }"
        class="inline-flex items-center min-h-11 text-sm font-semibold underline decoration-primary decoration-2 underline-offset-[5px] hover:decoration-chocolate"
      >
        Shop the range
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { RouterLink } from "vue-router";
import { ExclamationTriangleIcon, CheckBadgeIcon } from "@heroicons/vue/24/outline";
import { allergenText } from "../../composables/useCatalog";
import { formatCategoryLabel, categoryBlurb } from "../../constants/cookieCategories";

const props = defineProps({
  cookie: {
    type: Object,
    required: true,
  },
});

const allergens = computed(() => allergenText(props.cookie));
const categoryLabel = computed(() => `${formatCategoryLabel(props.cookie.category)} range`);
const blurb = computed(() => categoryBlurb(props.cookie.category));
const ingredients = computed(() =>
  (props.cookie.ingredients || "")
    .split(",")
    .map((s) => s.trim())
    .filter(Boolean)
    .map((s) => s.charAt(0).toUpperCase() + s.slice(1)),
);
</script>
