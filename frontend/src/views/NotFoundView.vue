<script setup>
/**
 * NotFoundView — any URL that matches no page. Prerendered to /404.html,
 * which static hosts serve with a real 404 status. Kept out of search
 * results (noindex, see router/routes.js).
 */
import { RouterLink } from "vue-router";
import { ArrowRightIcon } from "@heroicons/vue/20/solid";
import BaseButton from "../components/ui/BaseButton.vue";

const suggestions = [
  { label: "All cookies", to: { name: "Products" } },
  { label: "About us", to: { name: "About" } },
  { label: "Questions and answers", to: { name: "Contact", hash: "#faq-title" } },
  { label: "Contact us", to: { name: "Contact" } },
];
</script>

<template>
  <section class="relative isolate overflow-hidden bg-tan pt-[calc(var(--nav-h)+4.8rem)] pb-20 md:pb-28">
    <div class="container-magic grid md:grid-cols-[1.1fr_1fr] items-center gap-10">
      <div class="animate-fade-up">
        <p class="text-sm font-bold text-chocolate/80">Error 404</p>
        <h1 class="mt-3 font-display text-display">
          This page has <span class="sticker">crumbled</span>
        </h1>
        <p class="prose-copy !text-chocolate/85 mt-6 !text-base">
          The link might be old, or the page has moved. Everything else is still fresh out of the oven.
        </p>
        <div class="mt-8 flex flex-wrap gap-3">
          <BaseButton :to="{ name: 'Products' }" size="lg">
            Shop cookies
            <ArrowRightIcon class="h-5 w-5" />
          </BaseButton>
          <BaseButton :to="{ name: 'Home' }" variant="outline" size="lg" class="border-chocolate/35">
            Back to home
          </BaseButton>
        </div>

        <nav aria-label="Popular pages" class="mt-12">
          <p class="text-sm font-bold text-chocolate/80">Or try one of these</p>
          <ul class="mt-2 flex flex-wrap gap-x-6">
            <li v-for="link in suggestions" :key="link.label">
              <RouterLink
                :to="link.to"
                class="inline-flex items-center min-h-11 font-semibold underline decoration-chocolate/40 decoration-2 underline-offset-[6px] hover:decoration-chocolate"
              >
                {{ link.label }}
              </RouterLink>
            </li>
          </ul>
        </nav>
      </div>

      <div class="relative hidden md:flex justify-end" aria-hidden="true">
        <img
          src="/img/cookie2.webp"
          alt=""
          width="794"
          height="900"
          class="w-full max-w-[40rem] rotate-12 drop-shadow-cookie animate-cookie-in"
        />
      </div>
    </div>
  </section>
</template>
