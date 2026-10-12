<script setup>
/**
 * PageHero — tan header band for inner pages (About, Contact).
 * Title goes in the default slot so pages can use the sticker lettering.
 * The image is a transparent, already-angled banner group; pass its
 * intrinsic size so the band doesn't jump when it loads.
 */
defineProps({
  text: { type: String, default: "" },
  image: { type: String, default: "/img/cookie.webp" },
  imageAlt: { type: String, default: "" },
  imageWidth: { type: Number, default: null },
  imageHeight: { type: Number, default: null },
});
</script>

<template>
  <section class="relative isolate overflow-hidden bg-tan pt-[calc(var(--nav-h)+4.8rem)] pb-16 md:pb-20">
    <div class="container-magic grid md:grid-cols-[1.1fr_1fr] items-center gap-8">
      <div class="animate-fade-up">
        <h1 class="font-display text-display">
          <slot />
        </h1>
        <p v-if="text" class="prose-copy !text-chocolate/85 mt-6 !text-base">{{ text }}</p>
        <div v-if="$slots.actions" class="mt-8 flex flex-wrap gap-3">
          <slot name="actions" />
        </div>
      </div>
      <div class="relative hidden md:flex justify-end" aria-hidden="true">
        <img
          :src="image"
          :alt="imageAlt"
          :width="imageWidth"
          :height="imageHeight"
          class="w-full max-w-[56rem] max-h-[36rem] object-contain drop-shadow-cookie animate-cookie-in"
        />
      </div>
    </div>
  </section>
</template>
