<script setup>
/**
 * Breadcrumb — accessible trail. Items come from seo/breadcrumbs.js, which
 * also feeds the BreadcrumbList structured data, so both say the same thing.
 * The last item is the current page.
 *
 *   <Breadcrumb :items="breadcrumbsFor(route)" />
 */
import { ChevronRightIcon } from "@heroicons/vue/20/solid";
import { RouterLink } from "vue-router";

defineProps({
  items: { type: Array, default: () => [] }, // [{ label, to }]
});
</script>

<template>
  <nav v-if="items.length" aria-label="Breadcrumb">
    <ol class="flex flex-wrap items-center gap-1 text-sm font-medium text-chocolate/80">
      <li v-for="(item, i) in items" :key="item.path" class="flex items-center gap-1 min-w-0">
        <RouterLink
          v-if="i < items.length - 1"
          :to="item.to"
          class="inline-flex items-center min-h-11 hover:text-chocolate hover:underline underline-offset-4"
        >
          {{ item.label }}
        </RouterLink>
        <span v-else class="text-chocolate truncate max-w-[25.6rem]" aria-current="page">{{ item.label }}</span>
        <ChevronRightIcon v-if="i < items.length - 1" class="h-4 w-4 shrink-0" aria-hidden="true" />
      </li>
    </ol>
  </nav>
</template>
