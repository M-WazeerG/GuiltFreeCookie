<script setup>
/**
 * CategoryFilter — pill-style category selector for the product list.
 * Scrolls sideways on small screens instead of wrapping into a wall.
 */
defineProps({
  modelValue: { type: String, default: 'all' },
  categories: { type: Array, default: () => [] }, // [{id,label}]
  counts: { type: Object, default: () => ({}) }, // optional {CategoryId: count}
})

const emit = defineEmits(['update:modelValue'])
</script>

<template>
  <div
    class="-mx-5 px-5 sm:mx-0 sm:px-0 flex gap-2 overflow-x-auto no-scrollbar snap-x lg:flex-wrap lg:overflow-visible"
    role="group"
    aria-label="Filter by range"
  >
    <button
      v-for="cat in categories"
      :key="cat.id"
      type="button"
      :aria-pressed="modelValue === cat.id"
      :class="[ 'snap-start shrink-0 inline-flex items-center gap-2 rounded-full h-11 pl-4 text-sm font-semibold transition-colors duration-200 whitespace-nowrap', counts[cat.id] !== undefined ? 'pr-2' : 'pr-4', modelValue === cat.id ? 'bg-chocolate text-cream-50' : 'bg-surface text-chocolate border border-cream-500/60 hover:border-chocolate/50', ]"
      @click="emit('update:modelValue', cat.id)"
    >
      {{ cat.label }}
      <span
        v-if="counts[cat.id] !== undefined"
        :class="[ 'min-w-[2.8rem] h-7 px-1.5 inline-flex items-center justify-center rounded-full text-sm tabular', modelValue === cat.id ? 'bg-cream-50/15 text-cream-50' : 'bg-chocolate/[0.08] text-chocolate/75', ]"
      >
        {{ counts[cat.id] }}
      </span>
    </button>
  </div>
</template>
