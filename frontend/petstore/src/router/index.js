import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import AnimalView from '../views/AnimalView.vue';
import ProductView from '../views/ProductView.vue';
import AdminPanel from '../views/AdminPanel.vue';
import { useAuthStore } from '../stores/auth.store';
import AnimalManagementView from '@/views/AnimalManagementView.vue';
import ProductManagement from '@/views/ProductManagement.vue';
import RegisterView from '@/views/RegisterView.vue';

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
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: { requiresAuth: true },
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
    meta: { requiresAuth: true },
  },

  {
    path: '/animalPanel',
    name: 'animalPanel',
    component: AnimalManagementView,
    meta: { requiresAuth: true },
  },

  {
    path: '/productPanel',
    name: 'productPanel',
    component: ProductManagement,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.user) {
    next('/login');
  } else {
    next();
  }
});

export default router;
