<script setup>
/**
 * AdminPageHeader — title, one-line description and an actions slot.
 */
defineProps({
  title: { type: String, required: true },
  description: { type: String, default: "" },
});
</script>

<template>
  <div class="flex flex-col sm:flex-row sm:items-end justify-between gap-4 mb-8">
    <div>
      <h1 class="text-base font-bold">{{ title }}</h1>
      <p v-if="description" class="mt-1 text-sm text-chocolate/70">{{ description }}</p>
    </div>
    <div v-if="$slots.default" class="flex items-center gap-2 shrink-0">
      <slot />
    </div>
  </div>
</template>
