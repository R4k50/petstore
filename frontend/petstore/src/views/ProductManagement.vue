<template>
    <router-link to="/admin">
        <el-page-header :icon="ArrowLeft">
            <template #content>
                <span class="text-large font-600 mr-3">powrót</span>
            </template>
        </el-page-header>
    </router-link>
    <h1>zarządzenie produktami</h1>
</template>

<script setup>
import { ArrowLeft } from '@element-plus/icons-vue'
</script>

<style scoped></style>