<template>
    <nav class="navbar">

        <div class="nav-center">
            <img src="../assets/logo.png" alt="Logo" class="logo" />
            <span class="petstore-name">Petstore</span>
        </div>

        <div class="nav-left">
            <router-link to="/" class="nav-item" style="color:#67C23A">Strona główna</router-link>
            <router-link to="/animals" class="nav-item">Zwierzęta</router-link>
            <router-link to="/products" class="nav-item">Produkty</router-link>
        </div>

        <router-link to="/login" v-if="user == null">
            <el-button type="primary" class="login-button">
                Logowanie
            </el-button>
        </router-link>

        <router-link to="/login" v-if="user != null">
            <el-button type="primary" @click="authStore.logout()" class="login-button">
                Wyloguj
            </el-button>
        </router-link>
    </nav>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth.store'
import { storeToRefs } from 'pinia';

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
</script>

<style scoped></style>
