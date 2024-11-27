<template>
  <div class="center">
    <h1>Produkty</h1>

    <ProductFilter @updateFilters="updateFilters" :categories="categories" />

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="products.length === 0">
      <el-empty description="Brak produktów spełniających kryteria wyszukiwania." />
    </div>

    <div v-else class="product-list">
      <div class="product-item" v-for="product in products" :key="product.id">
        <Product :product="product" />
        <el-button type="primary" @click="openModal(product.id)">Zobacz szczegóły</el-button>
      </div>
    </div>

    <Pagination 
      :totalElements="totalElements" 
      :itemsPerPage="itemsPerPage" 
      :currentPage="currentPage" 
      @page-change="goToPage" 
    />

    <ProductDetails v-if="showModal" :productId="selectedProductId" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import ProductDetails from '@/components/ProductDetails.vue';
import Product from '@/components/Product.vue';
import ProductFilter from '@/components/ProductFilter.vue';
import Pagination from '@/components/Pagination.vue';

const products = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(8);
const totalElements = ref(0);

const showModal = ref(false);
const selectedProductId = ref(null);
const categories = ref([
  { id: 1, name: 'Zabawki' },
  { id: 2, name: 'Akcesoria' },
  { id: 3, name: 'Higiena' },
  { id: 4, name: 'Jedzenie' },
  { id: 5, name: 'Psy' },
  { id: 6, name: 'Koty' },
  { id: 7, name: 'Ptaki' },
  { id: 8, name: 'Gryzonie' },
]);

const filters = ref({
  name: '',
  maxQuantity: null,
  minPrice: null,
  maxPrice: null,
  sortByPrice: null,
  categories: [], // New field for selected categories
});

const buildSearchQuery = () => {
  const queries = [];

  if (filters.value.name) {
    queries.push(`name:*${filters.value.name}*`);
  }
  if (filters.value.minPrice !== null) {
    queries.push(`price>${filters.value.minPrice}`);
  }
  if (filters.value.maxPrice !== null) {
    queries.push(`price<${filters.value.maxPrice}`);
  }
  if (filters.value.maxQuantity !== null || filters.value.minQuantity !== null) {
    if (filters.value.maxQuantity === 0) {
      queries.push(`quantity:0`);
    } else if (filters.value.maxQuantity === 10) {
      queries.push(`quantity>0`);
      queries.push(`quantity<11`);
    } else if (filters.value.maxQuantity > 10) {
      queries.push(`quantity>10`);
    }
  }
  if (filters.value.categories.length > 0) {
    const categoryQueries = filters.value.categories.map(category => `categories;${category}`);
    queries.push(`(${categoryQueries.join(',')})`);
  }

  return queries.join(',');
};

const fetchData = async (page) => {
  try {
    loading.value = true;
    const searchQuery = buildSearchQuery();

    const sortOrder = filters.value.sortByPrice === 'priceAsc' ? 'price,asc' : 
                      filters.value.sortByPrice === 'priceDesc' ? 'price,desc' : 
                      'id,asc';

    const response = await axios.get('/api/products', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
        sort: sortOrder,
        search: searchQuery,
      },
    });

    products.value = response.data.content;
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch (err) {
    error.value = 'Nie udało się załadować produktów';
  } finally {
    loading.value = false;
  }
};

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchData(currentPage.value);
};

const openModal = (productId) => {
  selectedProductId.value = productId;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const goToPage = (page) => {
  if (page < 1) return;
  currentPage.value = page;
  fetchData(currentPage.value);
};

onMounted(() => fetchData(currentPage.value));
</script>

<style scoped>
h1 {
  font-size: 2em;
  color: #333;
}

.center {
  margin-bottom: 2px;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.product-list {
  display: flex;
  flex-wrap: wrap; 
  justify-content: center; 
}

.product-item {
  border: 1px solid #dcdfe6;
  padding: 20px;
  border-radius: 8px;
  width: 20%; 
  margin: 10px;
  text-align: center;
  box-sizing: border-box; 
  height: 300px; 
}

.product-item img {
  max-width: 100%;
  height: auto;
}
</style>
