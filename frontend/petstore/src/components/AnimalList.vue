<template>
  <div>
    <div v-if="loading">Ładowanie danych...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else-if="animals.length === 0">
      <el-empty description="Brak pozycji spełniających kryteria wyszukiwania" />
    </div>

    <div v-else style="width: 1200px">
      <el-table :data="animals" class="product-table" @sort-change="handleSortChange">
        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column label="Obraz" width="120">
          <template #default="{ row }">
            <img :src="getImageUrl(row.img)" alt="Animal Image" class="thumbnail" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Nazwa"></el-table-column>
        <el-table-column prop="price" label="Cena" sortable></el-table-column>
        <el-table-column prop="quantity" label="Ilość" sortable></el-table-column>
        <el-table-column label="Kategorie">
          <template #default="{ row }">
            {{ row.categories.map(category => category.name).join(', ') }}
          </template>
        </el-table-column>
        <el-table-column label="Sektor">
          <template #default="{ row }">
            <div v-if="row.sector">
              {{ row.sector.name }}
            </div>
            <div v-else>
              Brak sektora
            </div>
          </template>
        </el-table-column>

        <el-table-column label="Akcje" width="200">
          <template #default="{ row }">
            <div class="actions">
              <el-button type="warning" :icon="Edit" circle @click="openEditDialog(row.id)"></el-button>
              <el-button type="danger" :icon="Delete" circle @click="deleteAnimal(row.id)" style="margin-left: 1px;">
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
        <p>Czy na pewno chcesz usunąć to zwierzę?</p>
        <div class="modal-footer">
          <el-button type="danger" :icon="Delete" @click="confirmDeleteAnimal">Usuń</el-button>
          <el-button type="info" @click="resetDialog">Anuluj</el-button>
        </div>
      </div>
    </div>

    <div class="modal-content" v-if="editDialogVisible" title="Edytuj zwierzę">
      <EditAnimal :animalId="animalToEdit" @close="editDialogVisible = false" @refresh="fetchData(currentPage)" />
    </div>
  </div>
</template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import AdminAnimalFilter from '@/components/AdminAnimalFilter.vue';
  import EditAnimal from '@/components/EditAnimal.vue';
  import Pagination from '@/components/Pagination.vue';
  import { ElNotification } from 'element-plus';
  import { Edit, Delete } from '@element-plus/icons-vue';
  
  const editDialogVisible = ref(false);
  const animalToEdit = ref(null);
  const confirmDeleteModalVisible = ref(false);
  const animalIdToDelete = ref(null);
  
  const animals = ref([]);
  const loading = ref(true);
  const error = ref(null);
  const currentPage = ref(1);
  const itemsPerPage = ref(15);
  const totalPages = ref(0);
  const totalElements = ref(0);
  
  const getImageUrl = (imageName) => {
    if (!imageName) {
      return null;
    }
    return `api/image/${imageName}`;
  };
  
  const currentSort = ref({ field: 'id', order: 'asc' });
  
  const handleSortChange = ({ prop, order }) => {
    currentSort.value = { field: prop, order };
    fetchData(currentPage.value);
  };
  
  const fetchData = async (page) => {
    try {
      loading.value = true;
      const sortOrder = currentSort.value.order === 'ascending' ? `${currentSort.value.field},asc` : `${currentSort.value.field},desc`;
  
      const response = await axios.get('/api/animals', {
        params: {
          page: page - 1,
          size: itemsPerPage.value,
          sort: sortOrder,
          search: searchQuery,
        },
      });
  
      animals.value = response.data.content;
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
  
  const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    fetchData(currentPage.value);
  };
  
  const openEditDialog = (id) => {
    animalToEdit.value = id;
    editDialogVisible.value = true;
  };
  
  const deleteAnimal = (animalId) => {
    animalIdToDelete.value = animalId;
    confirmDeleteModalVisible.value = true;
  };
  
  const confirmDeleteAnimal = async () => {
    try {
      await axios.delete(`/api/animal/${animalIdToDelete.value}`);
  
      animals.value = animals.value.filter(animal => animal.id !== animalIdToDelete.value);
      totalElements.value -= 1;
  
      ElNotification.success({
        title: 'Sukces',
        message: 'Zwierzę zostało usunięte',
      });
  
      resetDialog();
    } catch (error) {
      ElNotification.error({
        title: 'Błąd',
        message: 'Nie udało się usunąć zwierzęcia. Spróbuj ponownie',
      });
    }
  };
  
  const resetDialog = () => {
    confirmDeleteModalVisible.value = false;
    animalIdToDelete.value = null;
  };
  
  onMounted(() => fetchData(currentPage.value));
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
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  contain: strict;
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
  