<template>
    <div class="overlay">
        <div class="create-product-container">
            <Icon icon="mdi:close" class="close" @click="closeDialog" />
            <h2 class="form-title">Dodaj sektor</h2>

            <el-form :model="sector">
                <el-form-item label="Nazwa" prop="name" :error="error?.name" :show-message="error?.name ? true : false">
                    <el-input v-model="sector.name" placeholder="Wprowadź nazwę sektora" class="create-product-input" />
                </el-form-item>

                <el-form-item label="Opis" prop="description" :error="error?.description"
                    :show-message="error?.description ? true : false">
                    <el-input type="textarea" v-model="sector.description" placeholder="Opis sektora" rows="3"
                        class="create-product-input" />
                </el-form-item>

                <div class="actions">
                    <el-button type="primary" @click="submitSector" class="create-product-button">Utwórz</el-button>
                    <el-button @click="closeDialog" class="cancel-button">Anuluj</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { Icon } from '@iconify/vue';

const emit = defineEmits();

const error = ref({});
const sector = reactive({
    name: '',
    description: ''
});

const submitSector = async () => {
    try {
        error.value = {};
        await axios.post('http://127.0.0.1:8000/sector', sectorToSend);

        ElNotification.success({
            title: 'Sukces',
            message: 'Sektor został pomyślnie dodany.',
            type: 'success',
            duration: 3000,
        });

        emit('close');
        window.location.reload();
    } catch (err) {
        if (err.response && err.response.data && err.response.data.errors) {
            error.value = err.response.data.errors;
        } else {
            ElNotification.error({
                title: 'Błąd',
                message: 'Nie udało się dodać sektora. Spróbuj ponownie.',
                type: 'error',
                duration: 3000,
            });
        }
    }
};

const closeDialog = () => {
    sector.name = '';
    sector.description = '';
    error.value = {};
    emit('close');
};
</script>

<style scoped>
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
    display: flex;
    justify-content: center;
    align-items: center;
}

.create-product-container {
    width: 500px;
    max-width: 90%;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
    z-index: 1000;
    display: flex;
    flex-direction: column;
    align-items: stretch;
    justify-content: flex-start;
    position: relative;
}

.close {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 1.8em;
    color: #333;
    cursor: pointer;
}

.form-title {
    text-align: center;
    margin-bottom: 20px;
}

.create-product-input {
    width: 100%;
    font-family: Quicksand;
}

.create-product-button {
    width: 100%;
    margin-top: 20px;
}

.el-form-item {
    width: 100%;
}

.el-form-item .el-form-item__label {
    text-align: left;
    margin-bottom: 5px;
    font-weight: 600;
}

.el-form-item .el-input,
.el-form-item .el-input-number,
.el-form-item .el-input__inner {
    width: 100%;
}

.el-form-item .el-button {
    width: 100%;
}

.cancel-button {
    width: 100%;
    margin-top: 20px;
    color: #4caf50;
    border: 1px solid #4caf50;
    background-color: transparent;
    font-weight: 600;
}

.cancel-button:hover {
    background-color: #e8f5e9;
    color: #4caf50;
    border-color: #4caf50;
}

.upload-demo {
    margin-bottom: 20px;
}

.actions {
    display: flex;
    justify-content: space-between;
}
</style>