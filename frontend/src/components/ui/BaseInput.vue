<script setup>
/**
 * BaseInput — labelled form field with error + help text.
 * Supports text, email, tel, password, number, textarea, select.
 */
import { computed, useSlots } from "vue";
import { ExclamationCircleIcon } from "@heroicons/vue/24/outline";
import { ChevronUpDownIcon } from "@heroicons/vue/20/solid";

const props = defineProps({
  modelValue: { type: [String, Number], default: "" },
  label: { type: String, default: "" },
  type: { type: String, default: "text" },
  placeholder: { type: String, default: "" },
  id: {
    type: String,
    default: () => `field-${Math.random().toString(36).slice(2, 8)}`,
  },
  name: { type: String, default: "" },
  required: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  error: { type: String, default: "" },
  help: { type: String, default: "" },
  autocomplete: { type: String, default: "off" },
  // For select
  options: { type: Array, default: () => [] }, // [{value,label}]
  // For textarea
  rows: { type: Number, default: 4 },
  // Light (cream) or dark (chocolate) variant
  variant: { type: String, default: "light" }, // light | dark
  // Accessible name when there's no visible label
  ariaLabel: { type: String, default: "" },
});

const emit = defineEmits(["update:modelValue", "blur", "focus"]);

const isTextarea = computed(() => props.type === "textarea");
const isSelect = computed(() => props.type === "select");
const fieldClass = computed(() =>
  props.variant === "dark" ? "field-dark" : "field",
);
const slots = useSlots();
const describedBy = computed(() =>
  props.error ? `${props.id}-error` : props.help ? `${props.id}-help` : undefined,
);

const onInput = (e) => {
  const v = e.target.value;
  emit("update:modelValue", props.type === "number" ? Number(v) : v);
};
</script>

<template>
  <div class="w-full">
    <label
      v-if="label"
      :for="id"
      :class="[ 'block text-sm font-semibold mb-1.5', variant === 'dark' ? 'text-cream/85' : 'text-chocolate/85', ]"
    >
      {{ label }}
      <span v-if="required" class="text-primary-700" aria-hidden="true">*</span>
    </label>

    <!-- Select -->
    <div v-if="isSelect" class="relative">
    <select
      :id="id"
      :name="name || id"
      :value="modelValue"
      :disabled="disabled"
      :class="[fieldClass, 'appearance-none pr-10 cursor-pointer', error ? 'border-red-600 ring-2 ring-red-600/15' : '']"
      :aria-label="ariaLabel || undefined"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="describedBy"
      @input="onInput"
      @blur="$emit('blur', $event)"
      @focus="$emit('focus', $event)"
    >
      <option v-for="opt in options" :key="opt.value" :value="opt.value">
        {{ opt.label }}
      </option>
    </select>
      <ChevronUpDownIcon class="pointer-events-none absolute right-3.5 top-1/2 -translate-y-1/2 h-5 w-5 text-chocolate/60" />
    </div>

    <!-- Textarea -->
    <textarea
      v-else-if="isTextarea"
      :id="id"
      :name="name || id"
      :value="modelValue"
      :placeholder="placeholder"
      :rows="rows"
      :disabled="disabled"
      :autocomplete="autocomplete"
      :class="[
        fieldClass,
        'resize-none',
        error ? 'border-red-600 ring-2 ring-red-600/15' : '',
      ]"
      :aria-label="ariaLabel || undefined"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="describedBy"
      @input="onInput"
      @blur="$emit('blur', $event)"
      @focus="$emit('focus', $event)"
    />

    <!-- Generic input -->
    <input
      v-else
      :id="id"
      :name="name || id"
      :type="type"
      :value="modelValue"
      :placeholder="placeholder"
      :disabled="disabled"
      :required="required"
      :autocomplete="autocomplete"
      :class="[fieldClass, error ? 'border-red-600 ring-2 ring-red-600/15' : '']"
      :aria-label="ariaLabel || undefined"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="describedBy"
      @input="onInput"
      @blur="$emit('blur', $event)"
      @focus="$emit('focus', $event)"
    />

    <!-- Error / Help text -->
    <p
      v-if="error"
      :id="`${id}-error`"
      class="mt-2 text-sm text-red-700 flex items-center gap-1.5 animate-fade-in"
    >
      <ExclamationCircleIcon class="h-4 w-4 shrink-0" />
      {{ error }}
    </p>
    <p
      v-else-if="help"
      :id="`${id}-help`"
      :class="[
        'mt-2 text-sm',
        variant === 'dark' ? 'text-cream/50' : 'text-chocolate/50',
      ]"
    >
      {{ help }}
    </p>
  </div>
</template>
