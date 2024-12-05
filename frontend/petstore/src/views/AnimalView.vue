<template>
  <div class="center">
    <h1>Zwierzęta</h1>
    <h4>Poznaj nasze zwierzątka. Jeśli któreś z nich Cię urzeknie, odwiedź je w naszym sklepie stacjonarnym.</h4>

    <AnimalFilter @updateFilters="updateFilters" :categories="categories" />

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="animals.length === 0">
      <el-empty description="Brak pozycji spełniających kryteria wyszukiwania" />
    </div>

    <div class="product-list">
      <div class="product-item" v-for="animal in animals" :key="animal.id">
        <Animal :animal="animal" />
        <el-button type="primary" @click="openModal(animal.id)">Zobacz szczegóły
          <Icon icon="heroicons-solid:external-link" style="margin-left:5px" />
        </el-button>
      </div>
    </div>

    <Pagination :totalElements="totalElements" :itemsPerPage="itemsPerPage" :currentPage="currentPage"
      @page-change="goToPage" />

    <AnimalDetails v-if="showModal" :animalId="selectedAnimalId" @close="closeModal" />

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import AnimalDetails from '@/components/AnimalDetails.vue';
import Animal from '@/components/Animal.vue';
import Pagination from '@/components/Pagination.vue';
import AnimalFilter from '@/components/AnimalFilter.vue';
import { Icon } from '@iconify/vue';

const animals = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(12);
const totalElements = ref(0);

const showModal = ref(false);
const selectedAnimalId = ref(null);
const categories = ref([]);

const filters = ref({
  name: '',
  minPrice: null,
  maxPrice: null,
  sortByPrice: null,
  categories: [],
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
  if (filters.value.categories.length > 0) {
    const categoryQueries = filters.value.categories.map(category => `categories;${category}`);
    queries.push(`(${categoryQueries.join(',')})`);
  }

  return queries.join(',');
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('/api/animal-categories', {
      params: { size: 9999 },
    });
    categories.value = response.data.content || [];
  } catch (error) {
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się załadować kategorii. Spróbuj ponownie.',
    });
    categories.value = [];
  }
};

const fetchData = async (page) => {
  try {
    loading.value = true;
    const searchQuery = buildSearchQuery();

    const sortOrder = filters.value.sortByPrice === 'priceAsc' ? 'price,asc' :
      filters.value.sortByPrice === 'priceDesc' ? 'price,desc' :
        'id,asc';

    const response = await axios.get('/api/animals', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
        sort: sortOrder,
        search: searchQuery,
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

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchData(currentPage.value);
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

onMounted(() => {
  fetchCategories();
  fetchData(currentPage.value);
});

</script>

<style scoped>
h1 {
  font-size: 2em;
  color: #333;
}

h4 {
  font-family: Quicksand;
  font-weight: 500;
  margin-bottom: 30px;
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