<template>
    <nav class="navbar">
        <div class="container">
            <div class="nav-center">
                <router-link to="/" class="logo-link">
                    <img src="../assets/logo.png" alt="Logo" class="logo" />
                    <span class="petstore-name">Petstore</span>
                </router-link>
            </div>

            <div class="nav-left">
                <router-link to="/" class="nav-item" active-class="active-link">Strona główna</router-link>
                <router-link to="/animals" class="nav-item" active-class="active-link">Zwierzęta</router-link>
                <router-link to="/products" class="nav-item" active-class="active-link">Produkty</router-link>

                <div v-if="user">
                    <router-link to="/admin" class="nav-item" active-class="active-link">Panel administracyjny</router-link>
                </div>
            </div>

            <div v-if="user == null">
                <router-link to="/login">
                    <el-button type="primary" class="login-button">Logowanie</el-button>
                </router-link>
            </div>

            <div v-else>
                <el-button type="primary" @click="handleLogout" class="login-button">Wyloguj</el-button>
            </div>
        </div>
    </nav>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
const router = useRouter();

const handleLogout = () => {
    authStore.logout();
    router.push('/login');
};
</script>

<style scoped>
.container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1200px;
    margin-inline: auto;
}
.navbar {
    padding: 10px 20px;
    background-color: #f4f4f4;
    border-bottom: 1px solid #dcdfe6;
}

.nav-left {
    display: flex;
}

.nav-item {
    margin-right: 20px;
    text-decoration: none;
    color: #333;
}

.nav-item:hover {
    color: #67C23A;
}

.nav-center {
    display: flex;
    align-items: center;
}

.logo-link {
    display: flex;
    align-items: center;
    text-decoration: none; /* Usuwamy podkreślenie linku */
}

.logo {
    height: 40px;
    margin-right: 10px;
}

.petstore-name {
    font-size: 24px;
    font-weight: bold;
    color: #333;
}

.login-button {
    margin-left: auto;
    font-family: "Quicksand", sans-serif;
}

.active-link {
    color: #67C23A;
    font-weight: bold;
}
</style>
