<template>
  <div class="overlay">
    <div class="create-product-container">
      <Icon icon="mdi:close" class="close" @click="closeDialog" />
      <h2 class="form-title">Dodaj zwierzę</h2>

      <el-form :model="animal">
        <el-form-item label="Nazwa" prop="name" :error="error?.name" :show-message="error?.name ? true : false">
          <el-input v-model="animal.name" placeholder="Wprowadź nazwę zwierzęcia" class="create-product-input" />
        </el-form-item>

        <el-form-item label="Cena" prop="price" :error="error?.price" :show-message="error?.price ? true : false">
          <el-input-number v-model="animal.price" :min="0" placeholder="Podaj cenę" class="create-product-input" />
        </el-form-item>

        <el-form-item label="Ilość" prop="quantity" :error="error?.quantity"
          :show-message="error?.quantity ? true : false">
          <el-input-number v-model="animal.quantity" :min="0" placeholder="Podaj ilość" class="create-product-input" />
        </el-form-item>

        <el-form-item label="Kategorie" prop="categories" :error="error?.categories"
          :show-message="error?.categories ? true : false">
          <el-select v-model="animal.categories" multiple placeholder="Wybierz kategorie" value-key="id"
            class="create-product-input" collapse-tags>
            <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category" />
          </el-select>
        </el-form-item>

        <el-form-item label="Sektor" prop="sector" :error="error?.sector" :show-message="error?.sector ? true : false">
          <el-select v-model="animal.sector" placeholder="Wybierz sektor" value-key="id" class="create-product-input">
            <el-option v-for="sector in sectors" :key="sector.id" :label="sector.name" :value="sector" />
          </el-select>
        </el-form-item>

        <div>
          <el-upload class="upload-demo" drag action="#" :on-change="handleImage">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              Drop file here or <em>click to upload</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                jpg/png files with a size less than 500kb
              </div>
            </template>
          </el-upload>
        </div>

        <div class="actions">
          <el-button type="primary" @click="submitAnimal" class="create-product-button">Utwórz</el-button>
          <el-button @click="closeDialog" class="cancel-button">Anuluj</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>
  
  <script setup>
  import { reactive, ref, onMounted } from 'vue';
  import axios from 'axios';
  import { ElNotification } from 'element-plus';
  import { Icon } from '@iconify/vue';
  import { UploadFilled } from '@element-plus/icons-vue'
  
  const emit = defineEmits();
  
  const image = ref(null);
  const error = ref({});
  const animal = reactive({
    name: '',
    price: null,
    quantity: null,
    categories: [],
    sector: null,
  });
  
  const categories = ref([]);
  const sectors = ref([]);

const fetchSectors = async () => {
  try {
    const response = await axios.get('/api/sectors'); 
    sectors.value = response.data.content;
  } catch (error) {
    console.error('Error fetching sectors:', error);
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się pobrać sektorów.',
      type: 'error',
      duration: 3000,
    });
  }
};

onMounted(() => {
  fetchCategories();
  fetchSectors(); 
});
  
  const fetchCategories = async () => {
    try {
      const response = await axios.get('/api/animal-categories');
      categories.value = response.data.content;
    } catch (error) {
      console.error('Error fetching categories:', error);
      ElNotification.error({
        title: 'Błąd',
        message: 'Nie udało się pobrać kategorii.',
        type: 'error',
        duration: 3000,
      });
    }
  };
  
  onMounted(() => {
    fetchCategories();
  });
  
  const handleImage = (event) => {
    const file = event.raw;
    if (file) {
      image.value = file;
    }
  };
  
  const submitAnimal = async () => {
    try {
        error.value = {};
        const categoriesToSend = animal.categories.map(category => ({ id: category.id }));
        const sectorToSend = animal.sector ? { id: animal.sector.id } : null;

        const animalToSend = {
            ...animal,
            categories: categoriesToSend,
            sector: sectorToSend
        };

        const formData = new FormData();
        const json = JSON.stringify(animalToSend);

        const blob = new Blob([json], {
            type: 'application/json'
        });

        formData.append("animal", blob);

        if (image.value) {
            formData.append('image', image.value);
        }

        await axios.post('http://127.0.0.1:8000/animal', formData);

        ElNotification.success({
            title: 'Sukces',
            message: 'Zwierzę dodano pomyślnie',
            type: 'success',
            duration: 3000,
        });

        emit('close');

        setTimeout(() => {
            window.location.reload();
        }, 1000);
    } catch (err) {
        if (err.response && err.response.data && err.response.data.errors) {
            error.value = err.response.data.errors;
        } else {
            ElNotification.error({
                title: 'Błąd',
                message: 'Nie udało się dodać zwierzęcia. Spróbuj ponownie.',
                type: 'error',
                duration: 3000,
            });
        }
    }
};
  
  const closeDialog = () => {
    animal.name = '';
    animal.price = null;
    animal.quantity = null;
    animal.categories = [];
    animal.sector = [];
    error.value = {};
    emit('close');
  };
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
  
  .create-product-container {
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
  
  .create-product-input {
    width: 100%;
    font-family: Quicksand;
  }
  
  .create-product-button {
    width: 100%;
    margin-top: 20px;
  }
  
  .el-form-item {
    width: 100%;
  }
  
  .el-form-item .el-form-item__label {
    text-align: left;
    margin-bottom: 5px;
    font-weight: 600;
  }
  
  .el-form-item .el-input,
  .el-form-item .el-input-number,
  .el-form-item .el-input__inner {
    width: 100%;
  }
  
  .el-form-item .el-button {
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
  
  .upload-demo {
    margin-bottom: 20px;
  }
  
  .actions {
    display: flex;
    justify-content: space-between;
  }
  </style>