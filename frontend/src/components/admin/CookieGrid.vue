<script setup>
/**
 * CookieGrid — admin cookie list. A table from md up, stacked rows below.
 */
import {
  PencilSquareIcon,
  TrashIcon,
  ArchiveBoxIcon,
  ArrowUturnLeftIcon,
} from "@heroicons/vue/24/outline";
import { cookieImage, imageFallback } from "../../composables/useCatalog";
import { useFormat } from "../../composables/useFormat";
import { formatCategoryLabel } from "../../constants/cookieCategories";
import IconAction from "./IconAction.vue";

defineProps({
  cookies: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["edit", "delete", "archive"]);
const { rand } = useFormat();
</script>

<template>
  <div class="card overflow-hidden">
    <table class="w-full text-left">
      <thead class="hidden md:table-header-group bg-cream-400/40 text-sm font-semibold text-chocolate/70">
        <tr>
          <th scope="col" class="py-3 pl-5 pr-3 font-semibold">Cookie</th>
          <th scope="col" class="py-3 px-3 font-semibold">Range</th>
          <th scope="col" class="py-3 px-3 font-semibold text-right">Price</th>
          <th scope="col" class="py-3 px-3 font-semibold">Status</th>
          <th scope="col" class="py-3 pl-3 pr-5 font-semibold text-right"><span class="sr-only">Actions</span></th>
        </tr>
      </thead>
      <tbody class="divide-y divide-chocolate/10">
        <tr
          v-for="cookie in cookies"
          :key="cookie.cookieId"
          class="grid grid-cols-[auto_1fr_auto] items-center gap-x-3 gap-y-1 p-4 md:table-row md:p-0 hover:bg-cream-400/25 transition-colors"
        >
          <!-- Cookie -->
          <td class="col-span-2 md:py-3 md:pl-5 md:pr-3">
            <div class="flex items-center gap-3.5 min-w-0">
              <div
                class="h-14 w-14 shrink-0 rounded-xl bg-chocolate/[0.06] flex items-center justify-center overflow-hidden"
                :class="{ 'grayscale opacity-60': cookie.archived }"
              >
                <img :src="cookieImage(cookie)" @error="imageFallback(cookie)($event)" alt="" class="h-[85%] w-[85%] object-contain" loading="lazy" />
              </div>
              <div class="min-w-0">
                <p class="font-semibold text-chocolate truncate" :class="{ 'text-chocolate/60': cookie.archived }">
                  {{ cookie.description }}
                </p>
                <p class="text-sm text-chocolate/65 truncate max-w-[44.8rem]">{{ cookie.ingredients }}</p>
              </div>
            </div>
          </td>

          <!-- Range -->
          <td class="hidden md:table-cell px-3 text-sm text-chocolate/85 whitespace-nowrap">
            {{ formatCategoryLabel(cookie.category) }}
          </td>

          <!-- Price -->
          <td class="row-start-1 col-start-3 text-right md:px-3 font-semibold tabular whitespace-nowrap">
            {{ rand(cookie.price) }}
          </td>

          <!-- Status -->
          <td class="col-span-2 md:col-span-1 md:px-3 flex md:table-cell items-center gap-2 text-sm">
            <span
              class="inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-sm font-semibold whitespace-nowrap"
              :class="cookie.archived ? 'bg-chocolate/[0.08] text-chocolate/75' : 'bg-emerald-800/15 text-emerald-900'"
            >
              <span class="h-1.5 w-1.5 rounded-full" :class="cookie.archived ? 'bg-chocolate/40' : 'bg-emerald-600'" />
              {{ cookie.archived ? "Archived" : "On menu" }}
            </span>
            <span class="md:hidden text-chocolate/65 truncate">{{ formatCategoryLabel(cookie.category) }}</span>
          </td>

          <!-- Actions -->
          <td class="row-start-2 col-start-3 md:pl-3 md:pr-5">
            <div class="flex justify-end gap-0.5">
              <IconAction label="Edit" :icon="PencilSquareIcon" @click="emit('edit', cookie)" />
              <IconAction
                :label="cookie.archived ? 'Put back on menu' : 'Archive'"
                :icon="cookie.archived ? ArrowUturnLeftIcon : ArchiveBoxIcon"
                @click="emit('archive', cookie)"
              />
              <IconAction label="Delete" tone="danger" :icon="TrashIcon" @click="emit('delete', cookie)" />
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
