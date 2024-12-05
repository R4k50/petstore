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
    1: 'info', 
    2: 'danger', 
    3: 'info',   
    4: 'warning', 
    5: 'danger',  
    6: 'primary', 
    7: 'success', 
    8: 'warning', 
  };
  </script>
  
  <style scoped>
  .flex {
    display: flex;
    gap: 0.5rem;
  }
  </style>
  