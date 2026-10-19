<script setup>
/**
 * ContactSection — chocolate contact panel + message form.
 * Validation is inline. There's no mail server behind the site, so the form
 * hands the message to the visitor's own email app, addressed and filled in.
 */
import { ref, reactive } from "vue";
import { RouterLink } from "vue-router";
import { PhoneIcon, EnvelopeIcon, MapPinIcon } from "@heroicons/vue/24/outline";
import { CheckIcon, ArrowUpRightIcon } from "@heroicons/vue/20/solid";
import BaseInput from "../../components/ui/BaseInput.vue";
import BaseButton from "../../components/ui/BaseButton.vue";
import { useScrollReveal } from "../../composables/useScrollReveal";
import { cleanEmail, cleanText, cleanMultiline, isEmail } from "../../utils/normalize";
import { BUSINESS } from "../../constants/business";

defineProps({
  // The Contact page has its own hero, so it hides this heading.
  showHeading: { type: Boolean, default: true },
});

const { vReveal } = useScrollReveal();

const blank = () => ({ name: "", email: "", subject: "", message: "" });
const form = reactive(blank());
const errors = reactive({});
const handedOff = ref(false);

const contactCards = [
  {
    icon: PhoneIcon,
    label: "Phone",
    value: BUSINESS.phone,
    sub: "Call or WhatsApp us",
    href: BUSINESS.phoneHref,
  },
  {
    icon: EnvelopeIcon,
    label: "Email",
    value: BUSINESS.email,
    sub: "We reply within 24 hours",
    href: `mailto:${BUSINESS.email}`,
  },
  {
    icon: MapPinIcon,
    label: "Address",
    value: "41 Caravel Road, Strandfontein",
    sub: "Cape Town, 7798",
    href: BUSINESS.mapsUrl,
  },
];

const validate = () => {
  Object.keys(errors).forEach((k) => delete errors[k]);
  form.name = cleanText(form.name);
  form.email = cleanEmail(form.email);
  form.subject = cleanText(form.subject);
  form.message = cleanMultiline(form.message);
  if (!form.name) errors.name = "Tell us your name.";
  if (!isEmail(form.email)) errors.email = "Enter an email like you@example.co.za.";
  if (form.message.length < 10) errors.message = "Add a little more detail so we can help.";
  return Object.keys(errors).length === 0;
};

const onSubmit = () => {
  if (!validate()) return;
  const subject = form.subject || `Message from ${form.name}`;
  const body = `${form.message}\n\n${form.name}\n${form.email}`;
  window.location.href =
    `mailto:${BUSINESS.email}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
  handedOff.value = true;
};

const startOver = () => {
  Object.assign(form, blank());
  handedOff.value = false;
};
</script>

<template>
  <section class="section bg-background relative" aria-labelledby="contact-title">
    <div class="container-magic">
      <div v-if="showHeading" v-reveal class="mb-12 max-w-2xl">
        <h2 id="contact-title" class="font-display text-display">
          Let's talk <span class="text-primary-700">cookies</span>
        </h2>
        <p class="prose-copy mt-4">
          Questions, custom orders or bulk boxes for an event. Our team will
          get back to you quickly.
        </p>
      </div>
      <h2 v-else id="contact-title" class="sr-only">Contact us</h2>

      <div
        v-reveal
        class="grid lg:grid-cols-[0.85fr_1.15fr] overflow-hidden rounded-[28px] bg-surface shadow-soft-lg"
      >
        <!-- Left: contact info -->
        <div class="relative isolate overflow-hidden bg-chocolate text-cream-100 p-7 sm:p-10 lg:p-12">
          <h3 class="text-base text-cream-50">Contact information</h3>
          <p class="mt-2 text-cream-100/75">Pick whatever's easiest. We usually answer within a few hours.</p>

          <ul class="mt-9 space-y-3">
            <li v-for="card in contactCards" :key="card.label">
              <a
                :href="card.href"
                :target="card.label === 'Address' ? '_blank' : undefined"
                :rel="card.label === 'Address' ? 'noopener' : undefined"
                class="group flex items-center gap-4 rounded-2xl border border-cream-50/10 bg-cream-50/[0.04] p-4 transition-colors hover:bg-cream-50/[0.09]"
              >
                <span class="h-12 w-12 shrink-0 rounded-full bg-primary/20 text-primary flex items-center justify-center">
                  <component :is="card.icon" class="h-5 w-5" />
                </span>
                <span class="min-w-0 flex-1">
                  <span class="block text-sm text-cream-100/65">{{ card.label }}</span>
                  <span class="block font-semibold text-cream-50 break-words">{{ card.value }}</span>
                  <span class="block text-sm text-cream-100/65">{{ card.sub }}</span>
                </span>
                <ArrowUpRightIcon class="h-5 w-5 shrink-0 text-cream-100/40 transition-colors group-hover:text-primary" />
              </a>
            </li>
          </ul>


        </div>

        <!-- Right: form -->
        <div class="p-7 sm:p-10 lg:p-12" aria-live="polite">
          <div v-if="handedOff" class="h-full flex flex-col items-start justify-center py-10">
            <span class="h-14 w-14 rounded-full bg-primary text-chocolate flex items-center justify-center">
              <CheckIcon class="h-7 w-7" />
            </span>
            <h3 class="mt-5 text-base">Almost there</h3>
            <p class="mt-2 prose-copy">
              Your email app should have opened with your message ready to go. Press send there and
              we'll reply within 24 hours.
            </p>
            <p class="mt-3 prose-copy">
              Nothing opened? Email
              <a :href="`mailto:${BUSINESS.email}`" class="font-bold underline underline-offset-4">{{ BUSINESS.email }}</a>
              or call us on
              <a :href="BUSINESS.phoneHref" class="font-bold underline underline-offset-4 whitespace-nowrap">{{ BUSINESS.phone }}</a>.
            </p>
            <div class="mt-7 flex flex-wrap gap-3">
              <BaseButton variant="outline" size="sm" @click="onSubmit">Open it again</BaseButton>
              <BaseButton variant="outline" size="sm" @click="startOver">Write a new message</BaseButton>
            </div>
          </div>

          <template v-else>
            <h3 class="text-base">Send us a message</h3>
            <form class="mt-7 space-y-5" novalidate @submit.prevent="onSubmit">
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
                <BaseInput
                  v-model="form.name"
                  label="Your name"
                  placeholder="Sarah Ndlovu"
                  autocomplete="name"
                  :error="errors.name"
                  required
                />
                <BaseInput
                  v-model="form.email"
                  label="Your email"
                  type="email"
                  placeholder="sarah@example.co.za"
                  autocomplete="email"
                  :error="errors.email"
                  required
                />
              </div>
              <BaseInput
                v-model="form.subject"
                label="Subject"
                placeholder="Custom cookie order for an event"
              />
              <BaseInput
                v-model="form.message"
                label="Message"
                type="textarea"
                :rows="5"
                placeholder="Tell us how we can help"
                :error="errors.message"
                required
              />
              <BaseButton type="submit" variant="primary" size="lg" block>
                Open in my email app
              </BaseButton>
              <p class="text-sm text-chocolate/75">
                This opens your email app with your message filled in, ready to send.
                We'll only use your details to reply to you. See our
                <RouterLink to="/privacy-policy" class="font-bold underline underline-offset-4">Privacy Policy</RouterLink>.
              </p>
            </form>
          </template>
        </div>
      </div>
    </div>
  </section>
</template>
