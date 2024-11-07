<template>
  <div class="modal">
    <div class="modal-content">
      <Icon icon="mdi:close" class="close" @click="$emit('close')" />
      <div class="modal-body">
        <img :src="getImageUrl(product.img)" alt="Product Image" class="product-image" />
        <div class="product-details">
          <div v-if="loading">Loading...</div>
          <div v-else-if="error">{{ error }}</div>
          <div v-else>
            <h1>{{ product.name }}</h1>
            <p>{{ product.description }}</p>
            <h2><strong>{{ product.price }} PLN</strong></h2>
            <p :class="quantityClass">{{ quantityMessage }}</p>
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

const props = defineProps(['productId']);
const product = ref({});
const loading = ref(true);
const error = ref(null);
const quantity = ref(0);

const fetchProduct = async (id) => {
  try {
    const response = await axios.get(`/api/product/${id}`);
    product.value = response.data;
    quantity.value = response.data.quantity || 0;
  } catch (err) {
    error.value = 'Failed to load product details';
  } finally {
    loading.value = false;
  }
};

const getImageUrl = (imageName) => {
  return `api/image/${imageName}`;
};

const quantityMessage = computed(() => {
  if (quantity.value === 0) {
    return 'Produkt tymczasowo niedostępny.';
  } else if (quantity.value <= 10) {
    return 'Ostatnie sztuki!';
  } else {
    return 'Produkt dostępny w dużej ilości.';
  }
});

const quantityClass = computed(() => {
  if (quantity.value === 0) {
    return 'text-red'; 
  } else if (quantity.value <= 10) {
    return 'text-orange';
  } else {
    return 'text-green'; 
  }
});

watch(() => props.productId, (newId) => {
  loading.value = true;
  fetchProduct(newId);
});

onMounted(() => {
  fetchProduct(props.productId);
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

.text-red {
  color: #F54444;
}

.text-orange {
  color: orange;
}

.text-green {
  color: green;
}
</style>
