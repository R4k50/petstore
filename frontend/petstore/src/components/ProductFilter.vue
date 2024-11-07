<template>
  <el-card class="filter-container" shadow="never">
    <div class="filters">
      <div class="filter-row">
        <el-input
          v-model="filters.name"
          placeholder="Wyszukaj po nazwie"
          clearable
          @clear="applyFilters"
          @input="applyFilters"
          class="name-filter"
        />

        <el-select
          v-model="filters.maxQuantity"
          placeholder="Dostępność produktów"
          @change="applyFilters"
          class="quantity-select"
        >
          <el-option label="Produkt niedostępny" :value="0"></el-option>
          <el-option label="Ostatnie sztuki" :value="10"></el-option>
          <el-option label="Produkt dostępny" :value="11"></el-option>
        </el-select>

        <el-slider
          v-model="priceRange"
          range
          :min="0"
          :max="500"
          step="1"
          @change="updatePriceFilter"
          class="price-slider"
          :format-tooltip="formatPriceTooltip"
        />

        <el-select
          v-model="filters.sortByPrice"
          placeholder="Sortuj według ceny"
          @change="applyFilters"
          class="sort-select"
        >
          <el-option :label="sortOptions[0].label" :value="sortOptions[0].value">
            <el-icon class="sort-icon"><ArrowUp /></el-icon> {{ sortOptions[0].label }}
          </el-option>
          <el-option :label="sortOptions[1].label" :value="sortOptions[1].value">
            <el-icon class="sort-icon"><ArrowDown /></el-icon> {{ sortOptions[1].label }}
          </el-option>
        </el-select>

        <el-button type="primary" @click="clearFilters">Wyczyść filtrowanie</el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';

const filters = ref({
  name: '',
  maxQuantity: null,
  minPrice: null,
  maxPrice: null,
  sortByPrice: null, 
});

const priceRange = ref([0, 500]);

const sortOptions = [
  { label: 'Cena rosnąco', value: 'priceAsc', icon: ArrowUp },
  { label: 'Cena malejąco', value: 'priceDesc', icon: ArrowDown },
];

const emit = defineEmits(['updateFilters']);

const updatePriceFilter = () => {
  filters.value.minPrice = priceRange.value[0];
  filters.value.maxPrice = priceRange.value[1];
  applyFilters();
};

const applyFilters = () => {
  emit('updateFilters', filters.value);
};

const clearFilters = () => {
  filters.value = { name: '', maxQuantity: null, minPrice: null, maxPrice: null, sortByPrice: null };
  priceRange.value = [0, 1000];
  applyFilters();
};

const formatPriceTooltip = (value) => `${value} zł`;
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
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  width: 100%;
}

.name-filter {
  flex: 2;
  max-width: 250px;
  font-family: 'Phudu', sans-serif !important;
}

.quantity-select {
  flex: 1;
  max-width: 200px;
}

.price-slider {
  flex: 2;
  margin-left: 8px;
  margin-right: 8px;
  max-width: 300px;
}

.sort-select {
  flex: 1;
  max-width: 200px;
}

.sort-icon {
  margin-right: 8px;
}
</style>