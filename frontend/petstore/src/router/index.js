import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import AnimalManagementView from '../views/AnimalManagementView.vue'
import AnimalView from '../views/AnimalView.vue'
import ProductView from '../views/ProductView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/animals',
    name: 'animals',
    component: AnimalView,
  },
  // {
  //   path: '/animals/:id',
  //   name: 'animal',
  //   component: AnimalManagementView,
  // },

  {
    path: '/products',
    name: 'product',
    component: ProductView,
  },
]

// Tworzenie instancji routera
const router = createRouter({
  history: createWebHashHistory(),
  routes, // Definiowane trasy
})

// Eksportowanie routera
export default router
