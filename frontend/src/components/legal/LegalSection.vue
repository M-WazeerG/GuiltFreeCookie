<script setup>
/**
 * LegalSection — one numbered section of a policy. Numbers stay visible so
 * people (and lawyers) can refer to "section 4" in an email.
 */
defineProps({
  id: { type: String, required: true },
  number: { type: Number, required: true },
  title: { type: String, required: true },
});
</script>

<template>
  <section :id="id" class="scroll-mt-[calc(var(--nav-h)+2.4rem)] border-t border-chocolate/15 pt-10 mt-12 first:mt-0">
    <h2 class="text-base font-bold">
      <span class="text-primary-700 tabular mr-2">{{ number }}.</span>{{ title }}
    </h2>
    <div class="legal-prose mt-4">
      <slot />
    </div>
  </section>
</template>
