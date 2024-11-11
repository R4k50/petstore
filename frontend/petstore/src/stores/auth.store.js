import { defineStore } from 'pinia';

import axios from 'axios';



export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({

        user: JSON.parse(localStorage.getItem('user')),
        returnUrl: null
    }),
    actions: {
        async login(email, password) {
            let user = null;
            try {
                const response = await axios.post('http://localhost:8000/login', {
                    email,
                    password
                });
                user = await response.data;
            } catch (err) {
                throw err;

            }

            this.user = await user;
            localStorage.setItem('user', JSON.stringify(user));

        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');

        },
        async register(email, password, name, surname, passwordConfirmation) {
            try {
                await axios.post('http://localhost:8000/register', {
                    email,
                    password,
                    name,
                    surname,
                    passwordConfirmation
                });
            } catch (err) {
                throw err;
            }

        }
    }
});