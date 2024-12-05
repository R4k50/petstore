<template>
    <div class="flex gap-2" v-if="categories && categories.length">
      <el-tag
        v-for="category in categories"
        :key="category.id"
        :type="categoryColors[category.id] || 'default'"
        effect="dark"
        round
      >
        {{ category.name }}
      </el-tag>
    </div>
  </template>
  
  <script setup>
  import { computed } from 'vue';
  
  const props = defineProps({
    categories: {
      type: Array,
      default: () => [],
    },
  });
  
  const categoryColors = {
    1: 'info', // zabawki
    2: 'danger', // akcesoria
    3: 'info',    // higiena
    4: 'warning', // jedzenie
    5: 'danger',  // psy
    6: 'primary', // koty
    7: 'success', // ptaki
    8: 'warning', // gryzonie
  };
  </script>
  
  <style scoped>
  .flex {
    display: flex;
    gap: 0.5rem;
  }
  </style>
  