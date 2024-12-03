<template>
  <div class="center">
    <h1>Zwierzęta</h1>
    <h4>Poznaj nasze zwierzątka. Jeśli któreś z nich Cię urzeknie, odwiedź je w naszym sklepie stacjonarnym.</h4>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>

    <div class="product-list">
      <div class="product-item" v-for="animal in animals" :key="animal.id">
        <Animal :animal="animal" />
        <el-button type="primary" @click="openModal(animal.id)">Zobacz szczegóły</el-button>
      </div>
    </div>

    <Pagination 
      :totalElements="totalElements" 
      :itemsPerPage="itemsPerPage" 
      :currentPage="currentPage" 
      @page-change="goToPage" 
    />

    <AnimalDetails v-if="showModal" :animalId="selectedAnimalId" @close="closeModal" />

    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import AnimalDetails from '@/components/AnimalDetails.vue';
import Animal from '@/components/Animal.vue';
import Pagination from '@/components/Pagination.vue';

const animals = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(8);
const totalElements = ref(0);

const showModal = ref(false);
const selectedAnimalId = ref(null);

const fetchData = async (page) => {
  try {
    loading.value = true;

    const response = await axios.get('/api/animals', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
      },
    });

    animals.value = response.data.content;
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch (err) {
    error.value = 'Nie udało się załadować zwierząt';
  } finally {
    loading.value = false;
  }
};

const openModal = (animalId) => {
  selectedAnimalId.value = animalId;
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

h4 {
  font-family: Quicksand;
  font-weight: 500;
  margin-bottom: 50px;
  color: rgb(59, 59, 59);
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