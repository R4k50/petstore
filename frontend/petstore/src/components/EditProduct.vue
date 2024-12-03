<template>
  <div class="overlay">
    <div class="create-product-container">
      <Icon icon="mdi:close" class="close" @click="closeDialog" />
      <h2 class="form-title">Edytuj produkt</h2>

      <el-form :model="form">
        <el-form-item label="Nazwa" prop="name">
          <el-input v-model="form.name" placeholder="Wprowadź nazwę produktu" class="create-product-input" />
        </el-form-item>

        <el-form-item label="Cena" prop="price">
          <el-input-number v-model="form.price" :min="0" placeholder="Podaj cenę" class="create-product-input" />
        </el-form-item>

        <el-form-item label="Opis" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="Wprowadź opis produktu" rows="3"
            class="create-product-input" />
        </el-form-item>

        <el-form-item label="Ilość" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0" placeholder="Podaj ilość" class="create-product-input" />
        </el-form-item>

        <el-form-item label="Kategorie" prop="categories">
          <el-select v-model="form.categories" multiple placeholder="Wybierz kategorie" value-key="id"
            class="create-product-input" collapse-tags>
            <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
          </el-select>
        </el-form-item>

        <div prop="image">
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
          <el-button type="primary" @click="saveProduct" class="create-product-button">Zapisz</el-button>
          <el-button @click="closeDialog" class="cancel-button">Anuluj</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, reactive } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { Icon } from '@iconify/vue';
import { UploadFilled } from '@element-plus/icons-vue'

const props = defineProps({
  productId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close', 'refresh']);

const form = reactive({
  name: '',
  price: 0,
  quantity: 0,
  description: '',
  image: null,
  categories: [],
});

const image = ref(null);
const categories = ref([]);

const loadCategories = async () => {
  try {
    const response = await axios.get('/api/product-categories');
    categories.value = response.data.content;
  } catch (error) {
    console.error('Błąd podczas ładowania kategorii:', error);
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się załadować kategorii.',
    });
  }
};

const loadProduct = async () => {
  try {
    const response = await axios.get(`http://127.0.0.1:8000/product/${props.productId}`);
    const productData = response.data;

    form.name = productData.name;
    form.price = productData.price;
    form.quantity = productData.quantity;
    form.description = productData.description;

    form.categories = productData.categories.map(category => category.id);
  } catch (error) {
    console.error('Błąd podczas ładowania produktu:', error);
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się załadować danych produktu.',
    });
  }
};

const saveProduct = async () => {
  try {
    const formData = new FormData();

    const productToSend = {
      name: form.name,
      price: form.price,
      quantity: form.quantity,
      description: form.description,
      categories: form.categories.map(id => ({ id }))
    };

    const json = JSON.stringify(productToSend);
    const blob = new Blob([json], {
      type: 'application/json',
    });

    formData.append('product', blob);

    if (image.value) {
      formData.append('image', image.value);
    }

    await axios.patch(`http://127.0.0.1:8000/product/${props.productId}`, formData);

    emit('refresh');
    closeDialog();

    ElNotification.success({
      title: 'Sukces',
      message: 'Produkt został zaktualizowany',
    });
  } catch (error) {
    console.error('Błąd podczas zapisywania produktu:', error);
    ElNotification.error({
      title: 'Błąd',
      message: 'Nie udało się zapisać produktu. Spróbuj ponownie',
    });
  }
};

const closeDialog = () => {
  emit('close');
};

const handleImage = (event) => {
  const file = event.raw;
  if (file) {
    image.value = file;
  }
};

watch(
  () => props.productId,
  (newProductId) => {
    if (newProductId) {
      loadProduct();
    }
  },
  { immediate: true }
);

loadCategories();
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
  /* Wyśrodkowanie kontenera */
  align-items: center;
  /* Wyśrodkowanie kontenera */
}

.create-product-container {
  width: 500px;
  /* Szerokość kontenera formularza */
  max-width: 90%;
  /* Maksymalna szerokość, aby formularz nie rozciągał się na całą szerokość ekranu */
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  /* Dopasowanie szerokości formularza do kontenera */
  justify-content: flex-start;
  /* Wyśrodkowanie zawartości w pionie */
  position: relative;
  /* Aby pozycjonowanie absolutne w .close działało w kontekście kontenera */
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  /* Przesunięcie w prawo w obrębie kontenera */
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
