<template>
  <div class="center">
    <h1>Produkty</h1>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else class="product-list">
      <div class="product-item" v-for="product in products" :key="product.id">
        <Product :product="product" />
        <el-button type="primary" @click="openModal(product.id)">Zobacz szczegóły</el-button>
      </div>
    </div>

    <div v-if="totalPages > 1" class="pagination">
      <el-pagination 
        layout="prev, pager, next" 
        :total="totalElements" 
        :page-size="itemsPerPage" 
        :current-page="currentPage" 
        @current-change="goToPage" 
      />
    </div>

    <ProductDetails v-if="showModal" :productId="selectedProductId" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import ProductDetails from '@/components/ProductDetails.vue';
import Product from '@/components/Product.vue';

const products = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(8); 
const totalPages = ref(0);
const totalElements = ref(0);

const showModal = ref(false);
const selectedProductId = ref(null);

const fetchData = async (page) => {
  try {
    loading.value = true;
    const response = await axios.get('/api/products', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
      },
    });
    products.value = response.data.content;
    totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch (err) {
    error.value = 'Nie udało się załadować produktów';
  } finally {
    loading.value = false;
  }
};

const openModal = (productId) => {
  selectedProductId.value = productId;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
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
  border: 1px solid #ccc;
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
