<script setup>
import { ClipboardDocumentListIcon } from "@heroicons/vue/24/outline";
import AdminPageHeader from "../components/admin/AdminPageHeader.vue";
</script>

<template>
  <div>
    <AdminPageHeader title="Orders" description="Track and fulfil customer orders." />

    <div class="card py-20 px-6 text-center">
      <ClipboardDocumentListIcon class="mx-auto h-10 w-10 text-chocolate/35" />
      <h2 class="mt-3 font-sans text-base font-semibold">No orders to show yet</h2>
      <p class="mt-1 text-chocolate/70 max-w-sm mx-auto">
        Orders will appear here once checkout goes live.
      </p>
    </div>
  </div>
</template>
