import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import AnimalView from '../views/AnimalView.vue';
import ProductView from '../views/ProductView.vue';
import AdminPanel from '../views/AdminPanel.vue';

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
  {
    path: '/products',
    name: 'product',
    component: ProductView,
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminPanel,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
