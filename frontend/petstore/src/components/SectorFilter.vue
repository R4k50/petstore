<template>
  <el-card class="filter-container" shadow="never">
    <div class="filters">
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Nazwa</label>
          <el-input v-model="filters.name" placeholder="Wpisz nazwę" clearable @clear="applyFilters"
            @input="applyFilters" class="name-filter">
            <template #append>
              <el-button @click="searchByName" style="width: 50px; height: 36px; padding: 0;">
                <Icon icon="mdi:search" class="search-icon" />
              </el-button>
            </template>
          </el-input>
        </div>
        <div class="filter-item">
          <el-button type="primary" @click="clearFilters" class="clear-button">Wyczyść filtrowanie
            <Icon icon="ic:round-clear" style="margin-left: 5px; font-size: large;" />
          </el-button>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import { Icon } from '@iconify/vue';

const props = defineProps({
  categories: {
    type: Array,
    default: () => [],
  },
});

const filters = ref({
  name: ''
});

const emit = defineEmits(['updateFilters']);

const applyFilters = () => {
  emit('updateFilters', filters.value);
};

const clearFilters = () => {
  filters.value = { name: '' };
  applyFilters();
};
</script>

<style scoped>
.filter-container {
  max-width: 85%;
  margin: 0 auto 20px;
  margin-bottom: -10px;
  text-align: left;
  background-color: #f4f4f4;
}

.filters {
  display: flex;
  flex-direction: column;
  gap: 20px;
  justify-content: center;
}

.filter-row {
  display: flex;
  gap: 20px;
  width: 100%;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  flex-direction: column;
  align-items: left;
  width: 100%;
  max-width: 250px;
}

.filter-price-item {
  display: flex;
  flex-direction: column;
  align-items: left;
  width: 100%;
  max-width: 600px;
}

.filter-row:first-child .filter-item {
  max-width: calc(50% - 10px);
}

.filter-row:nth-child(2) .filter-item {
  max-width: calc(100% - 25px);
}

.filter-row:nth-child(3) .filter-item {
  max-width: 100%;
}

.filter-row:nth-child(4) .filter-item {
  max-width: calc(50% - 10px);
}

.filter-row:nth-child(5) .filter-item {
  max-width: 100%;
}

.filter-label {
  font-size: 12px;
  font-weight: normal;
  margin-bottom: 8px;
  color: #666;
  text-align: left;
}

.filter-header {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: -5px;
}

.name-filter ::placeholder {
  font-family: Quicksand !important;
  font-weight: 500;
}

.name-filter input::placeholder,
.quantity-select,
.sort-select,
.category-select {
  width: 100%;
  font-family: Quicksand;
  font-weight: 500;
}

.price-slider {
  width: 100%;
  margin-left: 10px;
}

.sort-icon {
  margin-right: 8px;
}

.clear-button {
  max-width: 180px;
  align-self: left;
  margin-top: 24px;
}

.el-divider {
  margin-top: 25px;
  margin-bottom: -8px;
}

.search-icon {
  font-size: 18px;
}

.sort-row {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  gap: 20px;
}

.price-input ::placeholder {
  width: 80px;
  font-family: Quicksand;
  font-weight: 500;
}

.price-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.price-input-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
  width: 60px;
}

.price-label {
  font-size: 12px;
  color: #757575;
}

.price-slider {
  flex-grow: 1;
  width: 300px;
}
</style>