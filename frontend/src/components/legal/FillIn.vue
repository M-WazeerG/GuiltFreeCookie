<script setup>
/**
 * FillIn — shows a business fact, or a highlighted blank when it hasn't
 * been provided yet (see constants/business.js). Never invents a value.
 */
defineProps({
  value: { type: [String, Number], default: null },
  label: { type: String, required: true },
});
</script>

<template>
  <span v-if="value">{{ value }}</span>
  <mark
    v-else
    class="rounded-md bg-primary/30 px-1.5 py-0.5 font-bold text-chocolate"
    :title="`To be completed: ${label}`"
  >[{{ label }}]</mark>
</template>
