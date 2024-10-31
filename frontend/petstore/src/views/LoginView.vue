<template>
    <div>
        <h2>Logowanie</h2>
        <el-input v-model="email" style="width: 240px" placeholder="podaj login" />
        <el-input v-model="password" style="width: 240px" type="password" placeholder="podaj hasło" show-password />
        <el-button type="primary" @click="login">Zaloguj się</el-button>

        <div v-if="loading">Loading...</div>
        <div v-else-if="error">{{ error }}</div>
        <ul v-else-if="Object.keys(userdata).length">
            <li>
                mail: {{ userdata.email }}
            </li>
            <li>
                imię: {{ userdata.name }}
            </li>
            <li>
                nazwisko: {{ userdata.surname }}
            </li>
            <li>
                token: {{ userdata.token }}
            </li>
        </ul>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const email = ref('');
const password = ref('');

const userdata = ref({}); // Initialize as an empty object to prevent null reference errors
const loading = ref(false); // set initial loading state to false
const error = ref(null);

const login = async () => {
    loading.value = true;
    error.value = null; // reset error state

    try {
        const response = await axios.post('http://localhost:8000/login', {
            email: email.value, // Use .value to access the ref's actual value
            password: password.value,
        });
        userdata.value = response.data;
    } catch (err) {
        error.value = err.response ? err.response.data.message : 'Error during login';
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped></style>
