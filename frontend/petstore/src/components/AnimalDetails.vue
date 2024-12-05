<template>
  <div class="modal">
    <div class="modal-content">
      <Icon icon="mdi:close" class="close" @click="$emit('close')" />
      <div class="modal-body">
        <img :src="getImageUrl(animal.img)" alt="Animal Image" class="product-image" />
        <div class="product-details">
          <div v-if="loading">Loading...</div>
          <div v-else-if="error">{{ error }}</div>
          <div v-else>
            <h1>{{ animal.name }}</h1>
            <AnimalCategoryTags :categories="animal.categories" />
            <h2><strong>{{ animal.price.toFixed(2) }} PLN</strong></h2>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';
import AnimalCategoryTags from '@/components/AnimalCategoryTags.vue';

const props = defineProps(['animalId']);
const animal = ref({});
const loading = ref(true);
const error = ref(null);

const fetchAnimal = async (id) => {
  try {
    const response = await axios.get(`/api/animal/${id}`);
    animal.value = response.data;
  } catch (err) {
    error.value = 'Failed to load animal details';
  } finally {
    loading.value = false;
  }
};

const getImageUrl = (imageName) => {
  if (!imageName) {
    return null;
  }
  return `api/image/${imageName}`;
};

watch(() => props.animalId, (newId) => {
  loading.value = true;
  fetchAnimal(newId);
});

onMounted(() => {
  fetchAnimal(props.animalId);
});
</script>

<style scoped>
h1 {
  font-size: 2em;
  color: #56A72B;
}

h2 {
  margin-top: 40px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 45%;
  position: relative;
}

.modal-body {
  display: flex;
  align-items: flex-start;
}

.product-image {
  max-width: 300px;
  margin-right: 20px;
}

.product-details {
  flex-grow: 1;
  margin-left: 5px;
  text-align: left;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

p {
  font-family: "Quicksand", sans-serif;
}

.close {
  position: absolute;
  top: 10px; 
  right: 20px; 
  font-size: 1.8em; 
  color: #333; 
  cursor: pointer;
}
</style>
