<template>
  <div>
    <SectorFilter @updateFilters="updateFilters" />

    <div v-if="loading">Ładowanie danych...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else-if="sectors.length === 0">
      <el-empty description="Brak pozycji spełniających kryteria wyszukiwania" />
    </div>

    <div v-else>
      <el-table :data="sectors" class="product-table" @sort-change="handleSortChange">
        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column prop="name" label="Nazwa"></el-table-column>
        <el-table-column prop="description" label="Opis"></el-table-column>
        <el-table-column prop="lastCare" label="Ostatnio oporządzone" sortable></el-table-column>
        <el-table-column prop="lastFeed" label="Ostatnio nakarmione" sortable></el-table-column>
        <el-table-column prop="animals" label="Zwierzęta">
          <template #default="{ row }">
            <div v-if="row.animals && row.animals.length">
              {{ row.animals.map(animal => animal.name).join(', ') }}
            </div>
            <div v-else>
              Brak zwierząt
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Akcje" width="300">
          <template #default="{ row }">
            <div class="actions">
              <el-button type="warning" @click="updateLastCare(row)" style="margin-right: -5px;"> Umyte
              </el-button>
              <el-button type="info" @click="updateLastFeed(row)" style="margin-right: 5px;"> Nakarmione
              </el-button>
              <el-button type="danger" :icon="Delete" circle @click="deleteSector(row.id)" style="margin-left: 1px;">
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
        <p>Czy na pewno chcesz usunąć ten sektor?</p>
        <div class="modal-footer">
          <el-button type="danger" :icon="Delete" @click="confirmDeleteSector">Usuń</el-button>
          <el-button type="info" @click="resetDialog">Anuluj</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import SectorFilter from '@/components/SectorFilter.vue';
import Pagination from '@/components/Pagination.vue';
import { ElNotification } from 'element-plus';
import { Edit, Delete } from '@element-plus/icons-vue';

const confirmDeleteModalVisible = ref(false);
const sectorIdToDelete = ref(null);

const sectors = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const totalElements = ref(0);

const filters = ref({
  name: '',
});

const animalsBySector = ref({});

const formatDate = (timestamp) => {
  if (!timestamp) return '-';
  const date = new Date(timestamp);

  const padZero = (num) => (num < 10 ? `0${num}` : num);

  const year = date.getFullYear();
  const month = padZero(date.getMonth() + 1);
  const day = padZero(date.getDate());
  const hours = padZero(date.getHours());
  const minutes = padZero(date.getMinutes());
  const seconds = padZero(date.getSeconds());

  return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
};

const fetchAnimals = async (sectorName) => {
  try {
    const response = await axios.get('/api/animals', {
      params: {
        page: 0,
        size: 10,
        search: `sector;${sectorName}`,
      },
    });

    return response.data.content || [];
  } catch {
    ElNotification.error({
      title: 'Błąd',
      message: `Nie udało się załadować zwierząt dla sektora ${sectorName}`,
    });
    return [];
  }
};

const fetchData = async (page) => {
  try {
    loading.value = true;
    const searchQuery = filters.value.name ? `name:*${filters.value.name}*` : '';
    const sortOrder = 'id,asc';

    const response = await axios.get('/api/sectors', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
        sort: sortOrder,
        search: searchQuery,
      },
    });

    const sectorData = response.data.content.map(sector => ({
      ...sector,
      animals: [],
      lastCare: formatDate(sector.lastCare),
      lastFeed: formatDate(sector.lastFeed),
    }));

    const animalsPromises = sectorData.map(sector => 
      fetchAnimals(sector.name).then(animals => {
        animalsBySector.value[sector.name] = animals;
        return { ...sector, animals };
      })
    );

    sectors.value = await Promise.all(animalsPromises);

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

const updateLastCare = (row) => {
  const currentTimestamp = new Date();
  const formattedDate = formatDate(currentTimestamp);
  row.lastCare = formattedDate;
  ElNotification.success({
    title: 'Sukces',
    message: `Zaktualizowano "Ostatnio oporządzone" dla sektora ${row.id}`,
  });

  axios.patch(`/api/sector/${row.id}`, { lastCare: currentTimestamp.toISOString() }).catch(() => {
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się zaktualizować "Ostatnio oporządzone". Spróbuj ponownie.',
    });
  });
};

const updateLastFeed = (row) => {
  const currentTimestamp = new Date();
  const formattedDate = formatDate(currentTimestamp);
  row.lastFeed = formattedDate;
  ElNotification.success({
    title: 'Sukces',
    message: `Zaktualizowano "Ostatnio nakarmione" dla sektora ${row.id}`,
  });

  axios.patch(`/api/sector/${row.id}`, { lastFeed: currentTimestamp.toISOString() }).catch(() => {
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się zaktualizować "Ostatnio nakarmione". Spróbuj ponownie.',
    });
  });
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

const deleteSector = (sectorId) => {
  sectorIdToDelete.value = sectorId;
  confirmDeleteModalVisible.value = true;
};

const confirmDeleteSector = async () => {
  try {
    await axios.delete(`/api/sector/${sectorIdToDelete.value}`);

    sectors.value = sectors.value.filter(sector => sector.id !== sectorIdToDelete.value);
    totalElements.value -= 1;

    ElNotification.success({
      title: 'Sukces',
      message: 'Sektor został pomyślnie usunięty',
    });

    resetDialog();
  } catch (error) {
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się usunąć sektora. Spróbuj ponownie',
    });
  }
};

const resetDialog = () => {
  confirmDeleteModalVisible.value = false;
  sectorIdToDelete.value = null;
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