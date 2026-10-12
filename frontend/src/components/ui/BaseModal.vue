<script setup>
/**
 * BaseModal — accessible teleport modal with backdrop blur + esc-to-close.
 *
 * Usage:
 *   <BaseModal :open="isOpen" @close="isOpen = false" title="Title">
 *     ...content...
 *   </BaseModal>
 */
import { watch, onUnmounted } from 'vue'
import { XMarkIcon } from '@heroicons/vue/24/outline'

const props = defineProps({
  open: { type: Boolean, default: false },
  title: { type: String, default: '' },
  size: { type: String, default: 'md' }, // sm | md | lg
  closeOnBackdrop: { type: Boolean, default: true },
})

const emit = defineEmits(['close'])
const titleId = `modal-${Math.random().toString(36).slice(2, 8)}`

const sizeClass = {
  sm: 'max-w-md',
  md: 'max-w-lg',
  lg: 'max-w-2xl',
}[props.size]

const onKey = (e) => {
  if (e.key === 'Escape' && props.open) emit('close')
}
watch(
  () => props.open,
  (v) => {
    if (typeof document === 'undefined') return
    document.body.style.overflow = v ? 'hidden' : ''
    if (v) document.addEventListener('keydown', onKey)
    else document.removeEventListener('keydown', onKey)
  },
)
onUnmounted(() => {
  if (typeof document !== 'undefined') {
    document.body.style.overflow = ''
    document.removeEventListener('keydown', onKey)
  }
})
</script>

<template>
  <Teleport to="body">
    <Transition name="overlay">
      <div
        v-if="open"
        class="fixed inset-0 z-[90] bg-chocolate/60 backdrop-blur-sm flex items-center justify-center p-4"
        @click.self="closeOnBackdrop && emit('close')"
      >
        <Transition name="modal" appear>
          <div
            v-if="open"
            role="dialog"
            aria-modal="true"
            :aria-labelledby="title ? titleId : undefined"
            :class="['w-full max-h-[90dvh] flex flex-col bg-background rounded-[20px] shadow-soft-xl', sizeClass]"
          >
            <div
              v-if="title || $slots.header"
              class="flex items-start justify-between gap-4 px-6 pt-6 pb-4 sm:px-7 sm:pt-7 shrink-0"
            >
              <h2 v-if="title" :id="titleId" class="text-base font-semibold tracking-[-0.015em] text-chocolate">{{ title }}</h2>
              <slot name="header" />
              <button
                type="button"
                class="-mr-2 -mt-1 h-10 w-10 shrink-0 flex items-center justify-center text-chocolate/60 hover:text-chocolate transition-colors rounded-full hover:bg-chocolate/[0.07]"
                aria-label="Close"
                @click="emit('close')"
              >
                <XMarkIcon class="h-5 w-5" />
              </button>
            </div>
            <div class="overflow-y-auto px-6 pb-6 sm:px-7 sm:pb-7" :class="{ 'pt-7': !title && !$slots.header }">
              <slot />
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>
