<template>
  <router-link to="/admin">
    <el-link :underline="false" :icon="ArrowLeft">
      powrót
    </el-link>
  </router-link>

  <h2>Zarządzanie zwierzętami</h2>

  <el-button @click="openModal" :icon="Plus" class="add-product-btn">
    Dodaj nowe zwierzę
  </el-button>

  <el-button @click="toggleAnimalList" class="toggle-list-btn" :icon="isAnimalListVisible ? ArrowUp : ArrowDown">
    {{ isAnimalListVisible ? 'Zwiń listę zwierząt' : 'Edytuj zwierzęta' }}
  </el-button>

  <div v-if="isAnimalListVisible">
    <AnimalList />
  </div>

  <div v-if="showModal">
    <AddAnimal @close="closeModal" />
  </div>

  <AnimalSectors />

</template>

<script setup>
import { ref } from 'vue';
import AnimalList from '@/components/AnimalList.vue';
import AddAnimal from '@/components/AddAnimal.vue';
import { ArrowLeft, Close, Plus } from '@element-plus/icons-vue';
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import AnimalSectors from '@/components/AnimalSectors.vue';

const showModal = ref(false);
const isAnimalListVisible = ref(false);

const openModal = () => {
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const toggleAnimalList = () => {
  isAnimalListVisible.value = !isAnimalListVisible.value;
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

.toggle-list-btn {
  margin-left: 7.5%;
  margin-bottom: 5px;
}

.el-link {
  margin-top: 10px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
</style>
