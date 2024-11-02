<template>
<div v-for="product in products" :key="product.id">
    <img :src="getImageUrl(product.img)" alt="Product Image" />
    <div>
      {{ product.name }} 
    </div>
    <div>
      ${{ product.price }}
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
</script>

<style scoped>
h1 {
  font-size: 2em;
  color: #333;
}
img {
  max-width: 100px;
  margin-right: 10px;
}
button {
  margin: 0 5px;
}
.center {
  margin-bottom: 2px;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.flex {
  display: flex;
  gap: 1em;
}
</style>