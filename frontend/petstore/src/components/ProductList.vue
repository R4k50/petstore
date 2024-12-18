<template>
  <div>
    <AdminProductFilter @updateFilters="updateFilters" :categories="categories"/>

    <div v-if="loading">Ładowanie danych...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else-if="products.length === 0">
      <el-empty description="Brak produktów spełniających kryteria wyszukiwania" />
    </div>

    <div v-else style="width: 1200px">
      <el-table :data="products" class="product-table" @sort-change="handleSortChange">
        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column label="Obraz" width="120">
          <template #default="{ row }">
            <img :src="getImageUrl(row.img)" alt="Product Image" class="thumbnail" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Nazwa" sortable></el-table-column>
        <el-table-column prop="price" label="Cena" sortable></el-table-column>
        <el-table-column prop="quantity" label="Ilość" sortable></el-table-column>
        <el-table-column label="Kategorie">
          <template #default="{ row }">
            {{ row.categories.map(category => category.name).join(', ') || 'Brak kategorii' }}
          </template>
        </el-table-column>
        <el-table-column label="Akcje" width="200">
          <template #default="{ row }">
            <div class="actions">
              <el-button type="warning" :icon="Edit" circle @click="openEditDialog(row.id)"></el-button>
              <el-button type="danger" :icon="Delete" circle @click="deleteProduct(row.id)" style="margin-left: 1px;">
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <Pagination :totalElements="totalElements" :itemsPerPage="itemsPerPage" :currentPage="currentPage"
      @page-change="goToPage" />

    <div v-if="confirmDeleteModalVisible" class="modal-overlay" @click="resetDialog">
      <div class="modal-content" @click.stop>
        <h3>Potwierdzenie usunięcia</h3>
        <p>Czy na pewno chcesz usunąć ten produkt?</p>
        <div class="modal-footer">
          <el-button type="danger" :icon="Delete" @click="confirmDeleteProduct">Usuń</el-button>
          <el-button type="info" @click="resetDialog">Anuluj</el-button>
        </div>
      </div>
    </div>

    <div class="modal-content" v-if="editDialogVisible" title="Edytuj produkt">
      <EditProduct :productId="productToEdit" @close="editDialogVisible = false" @refresh="fetchData(currentPage)" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import AdminProductFilter from '@/components/AdminProductFilter.vue';
import EditProduct from '@/components/EditProduct.vue';
import Pagination from '@/components/Pagination.vue';
import { ElNotification } from 'element-plus';
import { Edit, Delete } from '@element-plus/icons-vue';

const editDialogVisible = ref(false);
const productToEdit = ref(null);
const confirmDeleteModalVisible = ref(false);
const productIdToDelete = ref(null);

const products = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const totalElements = ref(0);
const categories = ref([]);

const filters = ref({
  name: '',
  maxQuantity: null,
  minPrice: null,
  maxPrice: null,
  sortByPrice: null,
  categories: [],
});

const getImageUrl = (imageName) => {
  if (!imageName) {
    return null;
  }
  return `api/image/${imageName}`;
};

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
  if (filters.value.maxQuantity !== null) {
    if (filters.value.maxQuantity === 0) {
      queries.push('quantity:0');
    } else if (filters.value.maxQuantity === 10) {
      queries.push('quantity>0');
      queries.push('quantity<11');
    } else if (filters.value.maxQuantity > 10) {
      queries.push('quantity>10');
    }
  }
  if (filters.value.categories.length > 0) {
    const categoryQueries = filters.value.categories.map(category => `categories;${category}`);
    queries.push(`(${categoryQueries.join(',')})`);
  }
  return queries.join(',');
};

const currentSort = ref({ field: 'id', order: 'ascending' });

const handleSortChange = ({ prop, order }) => {
  currentSort.value = { field: prop, order };
  fetchData(currentPage.value);
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('/api/product-categories', {
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
    const sortOrder = currentSort.value.order === 'ascending' ? `${currentSort.value.field},asc` : `${currentSort.value.field},desc`;

    const response = await axios.get('/api/products', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
        sort: sortOrder,
        search: searchQuery,
      },
    });

    products.value = response.data.content;
    totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch {
    error.value = 'Nie udało się załadować danych z bazy';
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się załadować danych. Spróbuj ponownie.',
    });
  } finally {
    loading.value = false;
  }
};

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchData(currentPage.value);
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  fetchData(currentPage.value);
};

const openEditDialog = (id) => {
  productToEdit.value = id;
  editDialogVisible.value = true;
};

const deleteProduct = (productId) => {
  productIdToDelete.value = productId;
  confirmDeleteModalVisible.value = true;
};

const confirmDeleteProduct = async () => {
  try {
    await axios.delete(`/api/product/${productIdToDelete.value}`);

    products.value = products.value.filter(product => product.id !== productIdToDelete.value);
    totalElements.value -= 1;

    ElNotification.success({
      title: 'Sukces',
      message: 'Produkt został usunięty',
    });

    resetDialog();
  } catch (error) {
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się usunąć produktu. Spróbuj ponownie',
    });
  }
};

const resetDialog = () => {
  confirmDeleteModalVisible.value = false;
  productIdToDelete.value = null;
};

onMounted(() => {
  fetchCategories();
  fetchData(currentPage.value);
});

</script>

<style scoped>
.product-table {
  margin-top: 35px;
  margin-bottom: 35px;
}

.actions {
  display: flex;
  align-items: center;
}

.actions .el-button {
  margin-right: 10px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 500px;
}

.modal-footer {
  margin-top: 20px;
  text-align: right;
}

.modal-footer el-button {
  margin-left: 10px;
}

.thumbnail {
  width: 45px;
  height: 45px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ddd;
}
</style>
