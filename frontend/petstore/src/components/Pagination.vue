<template>
  <div v-if="totalPages > 1">
    <el-pagination
      layout="prev, pager, next"
      :total="totalElements"
      :page-size="itemsPerPage"
      :current-page="currentPage"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue';

const props = defineProps({
  totalElements: {
    type: Number,
    required: true
  },
  itemsPerPage: {
    type: Number,
    required: true
  },
  currentPage: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['page-change']);

const totalPages = computed(() => {
  return Math.ceil(props.totalElements / props.itemsPerPage);
});

const handlePageChange = (page) => {
  emit('page-change', page);
};
</script>
