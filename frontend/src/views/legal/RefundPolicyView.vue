<script setup>
/**
 * Refund Policy — for perishable food. Follows the CPA's implied warranty
 * of quality (section 56: the customer chooses repair, replacement or
 * refund for defective goods) and ECTA's exclusion of perishable food from
 * the cooling-off period (section 42).
 */
import { RouterLink } from "vue-router";
import LegalPage from "../../components/legal/LegalPage.vue";
import LegalSection from "../../components/legal/LegalSection.vue";
import { BUSINESS } from "../../constants/business";

const sections = [
  { id: "something-wrong", title: "If something is wrong with your order" },
  { id: "how-to", title: "How to let us know" },
  { id: "cancel", title: "Cancelling an order" },
  { id: "change-of-mind", title: "Change of mind" },
  { id: "delivery-problems", title: "Delivery problems" },
  { id: "custom", title: "Custom and bulk orders" },
  { id: "how-refunds-work", title: "How refunds are paid" },
  { id: "your-rights", title: "Your legal rights" },
];

const summary = [
  "Damaged, wrong, missing or not fresh? Send us a photo and we'll replace it or refund you, whichever you prefer.",
  "Tell us as soon as you can, ideally within 48 hours of delivery, so we can check the batch.",
  "You can cancel for a full refund until we start baking your order.",
  "Because cookies are perishable, we can't take returns for a change of mind once they've been baked.",
];
</script>

<template>
  <LegalPage title="Refund Policy" :summary="summary" :sections="sections">
    <LegalSection id="something-wrong" :number="1" title="If something is wrong with your order">
      <p>We'll put it right if your order:</p>
      <ul>
        <li>arrives damaged (more than the odd crumb);</li>
        <li>is missing items, or has the wrong items;</li>
        <li>isn't fresh, or isn't of the quality you'd reasonably expect;</li>
        <li>doesn't match its description, including the ingredients and allergens we listed.</li>
      </ul>
      <p>
        <strong>You choose whether we replace the item or refund you.</strong> You won't need to send
        perishable food back to us; a photo is usually enough.
      </p>
    </LegalSection>

    <LegalSection id="how-to" :number="2" title="How to let us know">
      <p>
        Email <a :href="`mailto:${BUSINESS.email}`">{{ BUSINESS.email }}</a>, or call or WhatsApp
        <a :href="BUSINESS.phoneHref">{{ BUSINESS.phone }}</a>, with:
      </p>
      <ul>
        <li>your order number and name;</li>
        <li>what's wrong, with a photo of the cookies and packaging;</li>
        <li>whether you'd like a replacement or a refund.</li>
      </ul>
      <p>
        Please tell us as soon as you notice, ideally within 48 hours of delivery, so we can check the batch
        and stop the problem happening again. Telling us later doesn't take away your rights (see section 8).
      </p>
    </LegalSection>

    <LegalSection id="cancel" :number="3" title="Cancelling an order">
      <p>
        You can cancel any order for a full refund until we start baking it. Contact us as soon as possible
        and we'll confirm whether it's still in time. Once your cookies have been baked, we can't cancel the
        order, because they can't be sold to anyone else.
      </p>
    </LegalSection>

    <LegalSection id="change-of-mind" :number="4" title="Change of mind">
      <p>
        Cookies are perishable food, so the law's seven-day cooling-off period for online purchases doesn't
        apply to them once they've been baked and sent, and for food-safety reasons we can't accept returns
        for a change of mind. This doesn't affect your right to a replacement or refund if something is
        wrong with your order.
      </p>
    </LegalSection>

    <LegalSection id="delivery-problems" :number="5" title="Delivery problems">
      <ul>
        <li><strong>Lost or very late in transit:</strong> we'll send a replacement or refund you in full, your choice.</li>
        <li>
          <strong>Nobody available, or a wrong address given at checkout:</strong> contact us and we'll try to
          arrange a new delivery. A redelivery fee may apply, and we'll tell you what it is before arranging it.
        </li>
        <li>
          <strong>Not delivered within 30 days</strong> (unless we agreed a different date with you): you may
          cancel and we'll refund everything you paid.
        </li>
      </ul>
    </LegalSection>

    <LegalSection id="custom" :number="6" title="Custom and bulk orders">
      <p>
        Custom, event and bulk orders follow the terms we agree with you in writing, including any deposit.
        Because they're made to your specifications, they can't be cancelled for a change of mind once
        baking has started. If anything is wrong with the quality, this policy applies in full.
      </p>
    </LegalSection>

    <LegalSection id="how-refunds-work" :number="7" title="How refunds are paid">
      <p>
        Refunds go back to the payment method you used. We aim to process them within 5 business days of
        approving them. Your bank may take a few more days to show the money in your account.
      </p>
    </LegalSection>

    <LegalSection id="your-rights" :number="8" title="Your legal rights">
      <p>
        This policy adds to your rights; it doesn't replace them. Under the Consumer Protection Act you're
        entitled to safe, good-quality goods that match their description, and to have defective goods
        replaced or refunded. If we can't agree on a solution, our
        <RouterLink to="/terms">Terms and Conditions</RouterLink> explain how to take your complaint further.
      </p>
    </LegalSection>
  </LegalPage>
</template>
