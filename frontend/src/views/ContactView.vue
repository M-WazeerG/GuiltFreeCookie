<script setup>
/**
 * ContactView — standalone Contact page.
 * Hero → contact panel + form → FAQ (most questions get answered there).
 */
import PageHero from "../components/sections/PageHero.vue";
import ContactSection from "../components/sections/ContactSection.vue";
import FaqSection from "../components/sections/FaqSection.vue";
</script>

<template>
  <div>
    <PageHero
      image="/img/banners/contact-banner.webp"
      :image-width="1400"
      :image-height="889"
      text="Custom orders, bulk boxes for an event, or a question about what's inside. We reply within 24 hours."
    >
      Let's talk <span class="sticker">cookies</span>
    </PageHero>

    <ContactSection :show-heading="false" />
    <FaqSection />
  </div>
</template>
