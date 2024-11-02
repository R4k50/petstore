<template>
    <div class="modal">
      <div class="modal-content">
        <span class="close" @click="$emit('close')">&times;</span>
        <div v-if="loading">Loading...</div>
        <div v-else-if="error">{{ error }}</div>
        <div v-else>
          <h1>{{ product.name }}</h1>
          <img :src="getImageUrl(product.img)" alt="Product Image" />
          <p>{{ product.description }}</p>
          <p>Price: ${{ product.price }}</p>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, watch, onMounted } from 'vue';
  import axios from 'axios';
  import { toRefs } from 'vue';
  
  const props = defineProps(['productId']);
  const product = ref({});
  const loading = ref(true);
  const error = ref(null);
  
  const fetchProduct = async (id) => {
    try {
      const response = await axios.get(`/api/product/${id}`);
      product.value = response.data;
    } catch (err) {
      error.value = 'Failed to load product details';
    } finally {
      loading.value = false;
    }
  };
  
  const getImageUrl = (imageName) => {
    return `/assets/images/${imageName}`;
  };
  
  // Watch for productId changes to fetch new data
  watch(() => props.productId, (newId) => {
    loading.value = true;
    fetchProduct(newId);
  });
  
  // Initial fetch
  onMounted(() => {
    fetchProduct(props.productId);
  });
  </script>
  
  <style scoped>
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
    max-width: 500px;
    width: 100%;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
  }
  
  .close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.5em;
    cursor: pointer;
  }

  img {
  max-width: 100px;
  margin-right: 10px;
}
  </style>
  