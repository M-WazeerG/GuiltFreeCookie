<script setup>
/**
 * HomeView — assembles the homepage sections in order:
 * Hero → ingredients → hot picks → signature drops → made for → CTA → FAQ
 */
import HeroBanner from "../components/sections/HeroBanner.vue";
import QualitySection from "../components/sections/QualitySection.vue";
import CookieShowcase from "../components/sections/CookieShowcase.vue";
import FeaturedCookies from "../components/sections/FeaturedCookies.vue";
import MadeForSection from "../components/sections/MadeForSection.vue";
import CtaBanner from "../components/sections/CtaBanner.vue";
import FaqSection from "../components/sections/FaqSection.vue";
</script>

<template>
  <div>
    <HeroBanner />
    <QualitySection />
    <CookieShowcase />
    <FeaturedCookies />
    <MadeForSection />
    <CtaBanner />
    <FaqSection />
  </div>
</template>
