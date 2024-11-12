<template>
  <div class="admin-panel">
    <el-card class="admin-card" shadow="hover">
      <h1 class="card-title">Panel Administracyjny</h1>
      <div class="button-container">
        <router-link to="/animalPanel" class="link">
          <div class="center">
            <Icon icon="mdi:dog" class="icon icon-green" />
            <el-button class="button-green">
              <span>Zarządzanie zwierzętami</span>
            </el-button>
          </div>
        </router-link>
        <router-link to="/productPanel" class="link">
          <div class="center">
            <Icon icon="mdi:cart" class="icon icon-blue" />
            <el-button class="button-blue">
              <span>Zarządzanie produktami</span>
            </el-button>
          </div>
        </router-link>
        <router-link to="/register" class="link">
          <div class="center">
            <Icon icon="mdi:account-plus" class="icon icon-purple" />
            <el-button class="button-purple">
              <span>Dodawanie użytkownika</span>
            </el-button>
          </div>
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
  outline: none;
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

.center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.icon {
  font-size: 50px;
}

.link{
  text-decoration: none;
}

.icon-green {
  color: #67C23A;
}

.icon-blue {
  color: #409EFF;
}

.icon-purple {
  color: #9B59B6;
}

</style>
