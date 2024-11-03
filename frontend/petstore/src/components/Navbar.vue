<template>
    <nav class="navbar">
        <div class="container">
            <div class="nav-center">
                <img src="../assets/logo.png" alt="Logo" class="logo" />
                <span class="petstore-name">Petstore</span>
            </div>

            <div class="nav-left">
                <router-link to="/" class="nav-item" style="color:#67C23A">Strona główna</router-link>
                <router-link to="/animals" class="nav-item">Zwierzęta</router-link>
                <router-link to="/products" class="nav-item">Produkty</router-link>
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
import { ElContainer } from 'element-plus';
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

.nav-center {
    display: flex;
    align-items: center;
}

.logo {
    height: 40px;
    margin-right: 10px;
}

.petstore-name {
    font-size: 24px;
    font-weight: bold;
}

.login-button {
    margin-left: auto;
    font-family: "Quicksand", sans-serif;
}
</style>
