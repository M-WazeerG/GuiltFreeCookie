<template>
  <div
    class="inline-flex h-14 items-center rounded-full border-2 border-chocolate/20 bg-surface"
    role="group"
    aria-label="Quantity"
  >
    <button
      type="button"
      class="h-full w-14 flex items-center justify-center rounded-l-full text-chocolate hover:bg-chocolate/[0.06] transition-colors disabled:opacity-35 disabled:cursor-not-allowed"
      :disabled="modelValue <= min"
      aria-label="Decrease quantity"
      @click="update(modelValue - 1)"
    >
      <MinusIcon class="h-5 w-5" />
    </button>

    <output class="w-10 text-center text-base font-semibold tabular" aria-live="polite">
      {{ modelValue }}
    </output>

    <button
      type="button"
      class="h-full w-14 flex items-center justify-center rounded-r-full text-chocolate hover:bg-chocolate/[0.06] transition-colors disabled:opacity-35 disabled:cursor-not-allowed"
      :disabled="modelValue >= max"
      aria-label="Increase quantity"
      @click="update(modelValue + 1)"
    >
      <PlusIcon class="h-5 w-5" />
    </button>
  </div>
</template>

<script setup>
import { MinusIcon, PlusIcon } from "@heroicons/vue/20/solid";

const props = defineProps({
  modelValue: {
    type: Number,
    default: 1,
  },
  min: {
    type: Number,
    default: 1,
  },
  max: {
    type: Number,
    default: 48,
  },
});

const emit = defineEmits(["update:modelValue"]);

const update = (val) => {
  emit("update:modelValue", Math.min(props.max, Math.max(props.min, val)));
};
</script>
