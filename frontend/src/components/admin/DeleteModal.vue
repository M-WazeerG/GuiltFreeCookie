<script setup>
import BaseModal from "../ui/BaseModal.vue";
import BaseButton from "../ui/BaseButton.vue";

defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  cookie: {
    type: Object,
    default: null,
  },
});

const emit = defineEmits(["confirm", "close"]);
</script>

<template>
  <BaseModal :open="open" title="Delete cookie?" size="sm" @close="emit('close')">
    <p class="text-chocolate/80 mb-6">
      Are you sure you want to delete
      <span class="font-semibold text-chocolate">{{ cookie?.description }}</span>? This
      can't be undone.
    </p>

    <div class="flex justify-end gap-3">
      <BaseButton variant="outline" size="sm" @click="emit('close')">Cancel</BaseButton>
      <BaseButton variant="danger" size="sm" @click="emit('confirm')">Delete</BaseButton>
    </div>
  </BaseModal>
</template>
