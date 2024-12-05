<template>
    <div class="overlay">
      <div class="edit-sector-container">
        <Icon icon="mdi:close" class="close" @click="closeDialog" />
        <h2 class="form-title">Edytuj sektor</h2>
  
        <el-form :model="sector">
          <el-form-item label="Nazwa" prop="name" :error="error?.name" :show-message="error?.name ? true : false">
            <el-input v-model="sector.name" placeholder="Wprowadź nową nazwę sektora" class="edit-sector-input" />
          </el-form-item>
  
          <el-form-item label="Opis" prop="description" :error="error?.description" :show-message="error?.description ? true : false">
            <el-input type="textarea" v-model="sector.description" placeholder="Opis sektora" rows="3" class="edit-sector-input" />
          </el-form-item>
  
          <div class="actions">
            <el-button type="primary" @click="saveSector" class="edit-sector-button">Zapisz</el-button>
            <el-button @click="closeDialog" class="cancel-button">Anuluj</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref, watch, onMounted } from 'vue';
  import axios from 'axios';
  import { ElNotification } from 'element-plus';
  import { Icon } from '@iconify/vue';
  
  const props = defineProps({
    sectorId: Number,
        required: true,
  });
  
  const emit = defineEmits(['close', 'refresh']);
  
  const error = ref({});
  const sector = reactive({
    name: '',
    description: '',
  });
  
  const loadSector = async () => {
    try {
      const response = await axios.get(`/api/sector/${props.sectorId}`);
      Object.assign(sector, response.data);
    } catch (error) {
      console.error('Błąd podczas ładowania sektora:', error);
      ElNotification.error({
        title: 'Błąd',
        message: 'Nie udało się załadować danych sektora.',
      });
    }
  };
  
  const saveSector = async () => {
    try {
      error.value = {};
  
      const sectorData = {
        name: sector.name,
        description: sector.description
      };
  
      await axios.patch(`/api/sector/${props.sectorId}`, sectorData);
  
      emit('refresh');
      closeDialog();
  
      ElNotification.success({
        title: 'Sukces',
        message: 'Sektor został pomyślnie zaktualizowany.',
      });
    } catch (err) {
      if (err.response && err.response.data && err.response.data.errors) {
        error.value = err.response.data.errors;
      } else {
        ElNotification.error({
          title: 'Błąd',
          message: 'Nie udało się zaktualizować sektora. Spróbuj ponownie.',
        });
      }
    }
  };
  
  const closeDialog = () => {
    emit('close');
  };
  
  watch(
    () => props.sectorId,
    (newSectorId) => {
      if (newSectorId) {
        loadSector(); 
      }
    },
    { immediate: true }
  );
  
  onMounted(() => {
    if (props.sectorId) {
      loadSector();
    }
  });
  </script>
  
  <style scoped>
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .edit-sector-container {
    width: 500px;
    max-width: 90%;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
    z-index: 1000;
    display: flex;
    flex-direction: column;
    align-items: stretch;
    justify-content: flex-start;
    position: relative;
  }
  
  .close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.8em;
    color: #333;
    cursor: pointer;
  }
  
  .form-title {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .edit-sector-input {
    width: 100%;
    font-family: Quicksand;
  }
  
  .edit-sector-button {
    width: 100%;
    margin-top: 20px;
  }
  
  .el-form-item {
    width: 100%;
  }
  
  .cancel-button {
    width: 100%;
    margin-top: 20px;
    color: #4caf50;
    border: 1px solid #4caf50;
    background-color: transparent;
    font-weight: 600;
  }
  
  .cancel-button:hover {
    background-color: #e8f5e9;
    color: #4caf50;
    border-color: #4caf50;
  }
  </style>
  