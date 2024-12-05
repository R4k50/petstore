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
          <el-button type="primary" @click="clearFilters" class="clear-button">Wyczyść filtrowanie</el-button>
        </div>
      </div>

      <div class="filter-price-item">
        <label class="filter-label">Cena</label>
        <div class="price-container">
          <div class="price-input-group">
            <div class="price-label">Od</div>
            <el-input v-model="filters.minPrice" placeholder="0" @input="onPriceInputChange" class="price-input">
            </el-input>
          </div>
          <div class="price-input-group">
            <div class="price-label">Do</div>
            <el-input v-model="filters.maxPrice" placeholder="1000" @input="onPriceInputChange" class="price-input">
            </el-input>
          </div>
          <el-slider v-model="priceRange" range :min="0" :max="1000" step="1" @change="updatePriceFilter"
            class="price-slider" :format-tooltip="formatPriceTooltip" :marks="marks" />
        </div>
      </div>

      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label filter-header">Filtry</label>
        </div>
      </div>

      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Ilość</label>
          <el-select v-model="filters.maxQuantity" placeholder="Wybierz dostępność" @change="applyFilters"
            class="quantity-select">
            <el-option label="Brak zwierząt na stanie" :value="0"></el-option>
            <el-option label="Mała liczba" :value="10"></el-option>
            <el-option label="Dużo zwierząt" :value="11"></el-option>
          </el-select>
        </div>

        <div class="filter-item">
          <label class="filter-label">Kategorie</label>
          <el-select v-model="filters.categories" multiple filterable :reserve-keyword="false"
            placeholder="Wybierz kategorie" @change="applyFilters" class="category-select">
            <el-option v-for="category in categories" :key="category.id" :label="category.name"
              :value="category.name" />
          </el-select>
        </div>
      </div>
      <div class="filter-row">
        <div class="filter-item">
          <label class="filter-label">Sektor</label>
          <el-select v-model="filters.sector" placeholder="Wybierz sektor" @change="applyFilters" class="sector-select">
            <el-option v-for="sector in sectors" :key="sector.id" :label="sector.name" :value="sector.name" />
          </el-select>
        </div>
      </div>

    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { Icon } from '@iconify/vue';

const props = defineProps({
  categories: {
    type: Array,
    default: () => [],
  },
  sectors: {
    type: Array,
    default: () => [],
  },
});

const filters = ref({
  name: '',
  maxQuantity: null,
  minPrice: null,
  maxPrice: null,
  categories: [],
  sector: '',
});

const priceRange = ref([0, 1000]);

const marks = {
  0: '0',
  1000: '1000',
};

const emit = defineEmits(['updateFilters']);

const applyFilters = () => {
  emit('updateFilters', filters.value);
};

const clearFilters = () => {
  filters.value = { name: '', maxQuantity: null, minPrice: null, maxPrice: null, categories: [], sector: '' };
  priceRange.value = [0, 1000];
  applyFilters();
};

const onPriceInputChange = () => {
  if (filters.value.minPrice > filters.value.maxPrice && filters.value.maxPrice !== null) {
    filters.value.minPrice = filters.value.maxPrice;
  }
  if (filters.value.minPrice < 0) filters.value.minPrice = 0;
  if (filters.value.maxPrice > 1000) filters.value.maxPrice = 1000;
  if (filters.value.minPrice !== null && filters.value.maxPrice === null) {
    filters.value.maxPrice = 1000;
  }
  priceRange.value = [filters.value.minPrice, filters.value.maxPrice];
  applyFilters();
};

const updatePriceFilter = () => {
  if (priceRange.value[0] > priceRange.value[1]) {
    filters.value.minPrice = priceRange.value[1];
    filters.value.maxPrice = priceRange.value[0];
  } else {
    filters.value.minPrice = priceRange.value[0];
    filters.value.maxPrice = priceRange.value[1];
  }

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

.filter-label {
  font-size: 12px;
  font-weight: normal;
  margin-bottom: 8px;
  color: #666;
  text-align: left;
}

.search-icon {
  font-size: 18px;
}

.clear-button {
  max-width: 150px;
  align-self: left;
  margin-top: 24px;
}

.sector-select {
  width: 49%;
  font-family: Quicksand;
}

.filter-container {
  max-width: 85%;
  margin: 0 auto 20px;
  margin-bottom: -10px;
  text-align: left;
  background-color: #f4f4f4;
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
  max-width: 150px;
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