<template>
  <router-link to="/admin">
    <el-link :underline="false" :icon="ArrowLeft">
      powrót
    </el-link>
  </router-link>

  <h2>Zarządzanie produktami</h2>

  <el-button @click="openModal" type="primary" class="add-product-btn" :icon="Plus">Dodaj produkt</el-button>

  <ProductList />

  <div v-if="showModal">
    <CreateProduct @close="closeModal" />
  </div>
</template>
  
  <script setup>
  import { ref } from 'vue';
  import ProductList from '@/components/ProductList.vue';
  import CreateProduct from '@/components/CreateProduct.vue';
  import { ArrowLeft, Close, Plus } from '@element-plus/icons-vue';
  
  const showModal = ref(false);
  
  const openModal = () => {
    showModal.value = true;
  };
  
  const closeModal = () => {
    showModal.value = false;
  };
  </script>
  
  <style scoped>
  h2 {
    text-align: center;
    margin-bottom: 30px;
  }
  
  .add-product-btn {
    margin-left: 7.5%;
    margin-bottom: 5px;
  }
  
  .el-link {
    margin-top: 30px;
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
  </style>
  