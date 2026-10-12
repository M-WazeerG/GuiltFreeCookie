<script setup>
/**
 * IconAction — small square icon button for table rows, with a tooltip
 * that also shows on keyboard focus. `tone="danger"` for destructive ones.
 */
defineProps({
  label: { type: String, required: true },
  icon: { type: [Object, Function], required: true },
  tone: { type: String, default: "default" }, // default | danger
  disabled: { type: Boolean, default: false },
  disabledReason: { type: String, default: "" },
});

defineEmits(["click"]);
</script>

<template>
  <span class="relative inline-flex group/action">
    <button
      type="button"
      :aria-label="label"
      :disabled="disabled"
      :class="[
        'h-11 w-11 inline-flex items-center justify-center rounded-lg transition-colors duration-150 disabled:opacity-35 disabled:cursor-not-allowed',
        tone === 'danger'
          ? 'text-red-700 hover:bg-red-700/10 disabled:hover:bg-transparent'
          : 'text-chocolate/75 hover:text-chocolate hover:bg-chocolate/[0.07] disabled:hover:bg-transparent',
      ]"
      @click="$emit('click')"
    >
      <component :is="icon" class="h-[18px] w-[18px]" />
    </button>
    <span
      role="tooltip"
      class="pointer-events-none absolute bottom-full right-1/2 translate-x-1/2 mb-1.5 whitespace-nowrap rounded-md bg-chocolate px-2 py-1 text-sm font-medium text-cream-50 opacity-0 translate-y-1 transition-all duration-150 group-hover/action:opacity-100 group-hover/action:translate-y-0 group-focus-within/action:opacity-100 group-focus-within/action:translate-y-0 z-10"
    >
      {{ disabled && disabledReason ? disabledReason : label }}
    </span>
  </span>
</template>
