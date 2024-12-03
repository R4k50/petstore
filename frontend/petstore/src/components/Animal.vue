<template>
    <div class="product-container">
      <img :src="getImageUrl(animal.img)" alt="Product Image" />
      <div class="product-name">{{ animal.name }}</div>
    </div>    
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const props = defineProps(['animal']);
  
  const getImageUrl = (imageName) => `api/image/${imageName}`;
  </script>
  
  <style scoped>
  .product-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  img {
    max-width: 100px;
    margin-bottom: 15px;
  }
  
  .product-name {
    margin-bottom: 10px;
    font-size: 1.2em;
  }
  
  .product-price {
    margin-bottom: 15px;
    font-weight: bold;
  }
  </style>
  