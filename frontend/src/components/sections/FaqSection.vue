<script setup>
/**
 * FaqSection — FAQ accordion with a sticky heading column on desktop.
 * Tailored for diabetic, keto, and health-conscious customers.
 */
import { RouterLink } from "vue-router";
import { ArrowRightIcon } from "@heroicons/vue/20/solid";
import AccordionList from "../../components/ui/AccordionList.vue";
import { faqs } from "../../data/faqs";
import { useScrollReveal } from "../../composables/useScrollReveal";

const { vReveal } = useScrollReveal();
</script>

<template>
  <section class="section bg-background" aria-labelledby="faq-title">
    <div class="container-magic grid lg:grid-cols-[0.8fr_1.2fr] gap-12 lg:gap-20">
      <div v-reveal class="lg:sticky lg:top-[calc(var(--nav-h)+3.2rem)] self-start">
        <h2 id="faq-title" class="font-display text-display">Good questions</h2>
        <p class="mt-5 text-base text-chocolate/80 max-w-sm">
          Sweeteners, carbs, allergens and keeping them fresh. If yours isn't
          here, ask us.
        </p>
        <RouterLink
          :to="{ name: 'Contact' }"
          class="group mt-5 inline-flex items-center min-h-11 gap-1.5 font-semibold underline decoration-primary decoration-2 underline-offset-[6px] hover:decoration-chocolate"
        >
          Ask a question
          <ArrowRightIcon class="h-5 w-5 transition-transform group-hover:translate-x-1" />
        </RouterLink>
      </div>

      <div v-reveal data-reveal-delay="100">
        <AccordionList :items="faqs" />
      </div>
    </div>
  </section>
</template>
