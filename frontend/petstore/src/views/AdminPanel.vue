<template>
  <div class="admin-panel">
    <el-card class="admin-card" shadow="hover">
      <h1 class="card-title">Panel Administracyjny</h1>
      <div class="button-container">
        <router-link to="/animalPanel">
          <el-button class="button-green">
            <Icon icon="mdi:dog" />
            Zarządzanie zwierzętami
          </el-button>
        </router-link>
        <router-link to="/productPanel">
          <el-button class="button-blue">
            <Icon icon="mdi:cart" />
            Zarządzanie produktami
          </el-button>
        </router-link>
        <router-link to="/register">
          <el-button class="button-purple">
            <Icon icon="mdi:account-plus" />
            Dodawanie użytkownika
          </el-button>
        </router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Icon } from '@iconify/vue';
</script>

<style scoped>
.admin-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
}

.admin-card {
  width: 800px;
  padding: 5px;
  border-radius: 8px;
}

.card-title {
  text-align: center;
  margin-bottom: 30px;
}

.button-container {
  display: flex;
  justify-content: center; 
  gap: 15px; 
}

.button-green {
  background-color: #67C23A;
  border-color: #67C23A;
  color: white;
}

.button-green:hover {
  background-color: #56A72B;
  border-color: #56A72B;
  color: white;
}

.button-blue {
  background-color: #409EFF;
  border-color: #409EFF;
  color: white;
}

.button-blue:hover {
  background-color: #3189D1;
  border-color: #3189D1;
  color: white;
}

.button-purple {
  background-color: #9B59B6;
  border-color: #9B59B6;
  color: white;
}

.button-purple:hover {
  background-color: #8E44AD;
  border-color: #8E44AD;
  color: white;
}

.close:hover {
  color: #56A72B;
}

.el-button {
  font-family: "Quicksand", sans-serif;
}

.el-button .iconify {
  margin-right: 8px;
}

.el-button:focus {
  box-shadow: 0 0 0 0.2rem rgba(103, 194, 58, 0.5);
}

.el-pagination {
  display: flex;
  justify-content: center;
  margin-top: 1em;
}
</style>
