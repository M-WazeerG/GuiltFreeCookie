<script setup>
/**
 * AccordionItem — single expandable FAQ row.
 * Designed to be used by parent AccordionList which manages open state.
 */
import { useId } from 'vue'
import { PlusIcon } from '@heroicons/vue/24/outline'

const props = defineProps({
  question: { type: String, required: true },
  answer: { type: String, required: true },
  isOpen: { type: Boolean, default: false },
})

defineEmits(['toggle'])

const uid = `acc-${useId()}`
</script>

<template>
  <div class="border-b border-chocolate/15">
    <h3 class="m-0 text-base">
      <button
        :id="`${uid}-btn`"
        type="button"
        class="group w-full flex items-center justify-between gap-6 py-6 text-left"
        :aria-expanded="props.isOpen"
        :aria-controls="`${uid}-panel`"
        @click="$emit('toggle')"
      >
        <span class="text-base font-semibold text-chocolate group-hover:text-primary-700 transition-colors">
          {{ question }}
        </span>
        <span
          :class="[
            'shrink-0 flex items-center justify-center h-10 w-10 rounded-full transition-all duration-300 ease-cookie',
            props.isOpen ? 'bg-chocolate text-cream-50 rotate-45' : 'bg-chocolate/[0.08] text-chocolate group-hover:bg-primary',
          ]"
          aria-hidden="true"
        >
          <PlusIcon class="h-5 w-5" />
        </span>
      </button>
    </h3>
    <Transition name="accordion">
      <div
        v-if="props.isOpen"
        :id="`${uid}-panel`"
        role="region"
        :aria-labelledby="`${uid}-btn`"
        class="pb-7 pr-14"
      >
        <p class="text-chocolate/80 text-base leading-relaxed max-w-[62ch]">{{ answer }}</p>
      </div>
    </Transition>
  </div>
</template>
