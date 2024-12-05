<template>
  <div>
    <SectorFilter @updateFilters="updateFilters" />

    <div v-if="loading">Ładowanie danych...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else-if="sectors.length === 0">
      <el-empty description="Brak pozycji spełniających kryteria wyszukiwania" />
    </div>

    <div v-else style="width: 1200px">
      <el-table :data="sectors" class="product-table" @sort-change="handleSortChange">
        <el-table-column type="expand">
          <template #default="{ row }">
            <div style="width: 1000px; margin-inline: auto">
              <h3>Zwierzęta</h3>
              <el-table :data="row.animals" :border="childBorder">
                <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
                <el-table-column label="Obraz" width="120">
                  <template #default="{ row }">
                    <img :src="getImageUrl(row.img)" alt="Animal Image" class="thumbnail" />
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="Nazwa" sortable></el-table-column>
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
                      <el-button type="danger" :icon="Delete" circle @click="deleteAnimal(row.id)"
                        style="margin-left: 1px;">
                      </el-button>
                    </div>
                  </template>
                </el-table-column>
                <div class="modal-content" v-if="editSectorDialogVisible" title="Edytuj sektor">
                  <EditSector :sectorId="sectorToEdit" @close="editSectorDialogVisible = false"
                    @refresh="fetchData(currentPage)" />
                </div>

              </el-table>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column prop="name" label="Nazwa sektora" sortable></el-table-column>
        <el-table-column prop="description" label="Opis">
          <template #default="{ row }">
            {{ row.description || 'Brak opisu' }}
          </template>
        </el-table-column>
        <el-table-column prop="lastCare" label="Ostatnio oporządzone" sortable>
          <template #default="{ row }">
            {{ row.lastCare != null ? (row.lastCare) : 'Puste' }}
          </template>
        </el-table-column>
        <el-table-column prop="lastFeed" label="Ostatnio nakarmione" sortable>
          <template #default="{ row }">
            {{ row.lastFeed != null ? (row.lastFeed) : 'Puste' }}
          </template>
        </el-table-column>
        <el-table-column label="Akcje" width="300">
          <template #default="{ row }">
            <div class="actions">
              <el-popconfirm title="Czy na pewno chcesz zaktualizować status 'Umyte'?" @confirm="updateLastCare(row)"
                confirm-button-text="Tak" cancel-button-text="Nie" icon="el-icon-question" icon-color="red">
                <template #reference>
                  <el-button type="warning" style="margin-right: -5px;"> Umyte </el-button>
                </template>
              </el-popconfirm>

              <el-popconfirm title="Czy na pewno chcesz zaktualizować status 'Nakarmione'?"
                @confirm="updateLastFeed(row)" confirm-button-text="Tak" cancel-button-text="Nie"
                icon="el-icon-question" icon-color="blue">
                <template #reference>
                  <el-button type="info" style="margin-right: -5px;"> Nakarmione </el-button>
                </template>
              </el-popconfirm>

              <el-button type="warning" :icon="Edit" circle @click="openEditSectorDialog(row.id)"
                style="margin-right: 5px;"></el-button>

              <el-button type="danger" :icon="Delete" circle @click="deleteSector(row.id)"
                style="margin-left: 1px;"></el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <Pagination :totalElements="totalElements" :itemsPerPage="itemsPerPage" :currentPage="currentPage"
      @page-change="goToPage" />

    <div class="modal-content" v-if="editSectorDialogVisible" title="Edytuj sektor" >
      <EditSector :sectorId="sectorToEdit" @close="editSectorDialogVisible = false" @refresh="fetchData(currentPage)" />
    </div>


    <div v-if="confirmDeleteAnimalModalVisible" class="modal-overlay" @click="resetAnimalDialog">
      <div class="modal-content" @click.stop>
        <h3>Potwierdzenie usunięcia</h3>
        <p>Czy na pewno chcesz usunąć to zwierzę?</p>
        <div class="modal-footer">
          <el-button type="danger" :icon="Delete" @click="confirmDeleteAnimal">Usuń</el-button>
          <el-button type="info" @click="resetAnimalDialog">Anuluj</el-button>
        </div>
      </div>
    </div>

    <div class="modal-content" v-if="editDialogVisible" title="Edytuj zwierzę">
      <EditAnimal :animalId="animalToEdit" @close="editDialogVisible = false" @refresh="fetchData(currentPage)" />
    </div>

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
import EditAnimal from './EditAnimal.vue';
import EditSector from './EditSector.vue';

const confirmDeleteAnimalModalVisible = ref(false);
const confirmDeleteModalVisible = ref(false);
const sectorIdToDelete = ref(null);

const editDialogVisible = ref(false);
const animalToEdit = ref(null);
const animalIdToDelete = ref(null);

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

const editSectorDialogVisible = ref(false);
const sectorToEdit = ref(null);

const openEditSectorDialog = (id) => {
  sectorToEdit.value = id;
  editSectorDialogVisible.value = true;
};

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
        size: 9999,
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

const getImageUrl = (imageName) => {
  if (!imageName) {
    return null;
  }
  return `api/image/${imageName}`;
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
      lastCare: sector.lastCare != null ? formatDate(sector.lastCare) : null,
      lastFeed: sector.lastFeed != null ? formatDate(sector.lastFeed) : null,
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

const confirmDeleteAnimal = async () => {
  try {
    await axios.delete(`/api/animal/${animalIdToDelete.value}`);

    const sectorName = Object.keys(animalsBySector.value).find(name =>
      animalsBySector.value[name].some(animal => animal.id === animalIdToDelete.value)
    );

    if (sectorName) {
      animalsBySector.value[sectorName] = await fetchAnimals(sectorName);
    }

    ElNotification.success({
      title: 'Sukces',
      message: 'Zwierzę zostało usunięte',
    });

    resetAnimalDialog();
  } catch (error) {
    console.error(error);
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się usunąć zwierzęcia. Spróbuj ponownie',
    });
  }
};


const openEditDialog = (id) => {
  animalToEdit.value = id;
  editDialogVisible.value = true;
};

const deleteAnimal = (animalId) => {
  animalIdToDelete.value = animalId;
  confirmDeleteAnimalModalVisible.value = true;
};

const resetDialog = () => {
  confirmDeleteModalVisible.value = false;
  sectorIdToDelete.value = null;
  editSectorDialogVisible.value = false;
};

const resetAnimalDialog = () => {
  confirmDeleteAnimalModalVisible.value = false;
  animalIdToDelete.value = null;
  fetchData(currentPage)
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