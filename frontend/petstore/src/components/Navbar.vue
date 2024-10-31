<template>
    <nav class="navbar">

        <div class="nav-center">
            <img src="../assets/logo.png" alt="Logo" class="logo" />
            <span class="petstore-name">Petstore</span>
        </div>

        <div class="nav-left">
            <router-link to="/" class="nav-item">Strona główna</router-link>
            <router-link to="/animals" class="nav-item">Zwierzęta</router-link>
            <router-link to="/products" class="nav-item">Produkty</router-link>
        </div>

        <router-link to="/login">
            <el-button type="primary" class="login-button">
                Logowanie
            </el-button>
        </router-link>
    </nav>
</template>

<script setup>
</script>

<style scoped>
</style>
