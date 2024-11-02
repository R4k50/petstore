<template>
    <div class="login-container">
        <h2 class="login-title">Logowanie</h2>
        <form @submit.prevent="handleLogin">
            <el-input v-model="email" class="login-input" placeholder="Podaj email" required />
            <el-input v-model="password" class="login-input" type="password" placeholder="Podaj hasło" show-password required />
            <el-button type="primary" class="login-button">Zaloguj się</el-button>
        </form>

        <div v-if="loading" class="loading-indicator">Ładowanie...</div>
        <div v-else-if="error" class="error-message">{{ error }}</div>
        <ul v-else-if="userdata" class="user-info">
            <li>Email: {{ userdata.email }}</li>
            <li>Imię: {{ userdata.name }}</li>
            <li>Nazwisko: {{ userdata.surname }}</li>
            <li>Token: {{ userdata.token }}</li>
        </ul>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();
const email = ref('');
const password = ref('');
const loading = ref(false);
const error = ref(null);
const userdata = ref(null); // Zmienna do przechowywania danych użytkownika

const handleLogin = async () => {
    loading.value = true;
    error.value = null;

    try {
        await authStore.login(email.value, password.value); // Wywołanie akcji logowania
        userdata.value = authStore.user; // Ustawienie danych użytkownika po pomyślnym logowaniu
        router.push('/admin');
    } catch (err) {
        error.value = 'Błąd podczas logowania!'; // Ustawienie błędu
        console.log(err);
    } finally {
        loading.value = false; // Ustawienie ładowania na false
    }
};
</script>

<style scoped>
.login-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.login-title {
    text-align: center;
    margin-bottom: 20px;
}

.login-input {
    width: 100%;
    margin-bottom: 15px;
}

.login-button {
    width: 100%;
}

.loading-indicator {
    text-align: center;
    margin-top: 15px;
}

.error-message {
    color: red;
    text-align: center;
    margin-top: 15px;
}

.user-info {
    margin-top: 15px;
    list-style-type: none;
    padding: 0;
}
</style>
