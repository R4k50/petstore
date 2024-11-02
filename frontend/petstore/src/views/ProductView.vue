<template>
  <div class="center">
    <h1>Produkty</h1>
    
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>
    
    <div v-else class="product-item">
      <img :src="getImageUrl(currentProduct.img)" :alt="currentProduct.name" />
      <h2>{{ currentProduct.name }}</h2>
      <p><strong>{{ currentProduct.price }} PLN</strong></p>
      <el-button type="primary" @click="openModal(currentProduct.id)">Zobacz szczegóły</el-button>
    </div>
    
    <div v-if="totalPages > 1" class="pagination">
      <el-button type="primary" @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">Poprzedni</el-button>
      <span>Strona {{ currentPage }} z {{ totalPages }}</span>
      <el-button type="primary" @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">Następny</el-button>
    </div>

    <ProductDetails v-if="showModal" :productId="selectedProductId" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import ProductDetails from '@/components/ProductDetails.vue';

const products = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(1); 
const totalPages = ref(0);

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
    error.value = null;
  } catch (err) {
    error.value = 'Nie udało się załadować produktów';
  } finally {
    loading.value = false;
  }
};

const getImageUrl = (imageName) => {
  return `/assets/images/${imageName}`;
};

const currentProduct = computed(() => {
  return products.value.length ? products.value[0] : null;
});

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
img {
  max-width: 100px;
  margin-bottom: 10px;
}
button {
  margin: 0 5px;
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
.pagination {
  margin-top: 1em;
}
</style>
