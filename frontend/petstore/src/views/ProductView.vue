<template>
  <div class="center">
    <h1>Produkty</h1>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else class="product-item" v-if="currentProduct">
      <Product :product="currentProduct" />
      <el-button type="primary" @click="openModal(currentProduct.id)">Zobacz szczegóły</el-button>
    </div>

    <div v-if="totalPages > 1" class="pagination">
      <el-pagination layout="prev, pager, next" :total="totalElements" :page-size="itemsPerPage"
        :current-page="currentPage" @current-change="goToPage" />
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
const itemsPerPage = ref(1);
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

const currentProduct = computed(() => products.value.length ? products.value[0] : null);

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

.product-item {
  border: 1px solid #ccc;
  padding: 1em;
  border-radius: 8px;
  max-width: 300px;
  margin: auto;
  text-align: center;
}
</style>
