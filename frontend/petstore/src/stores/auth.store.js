import { defineStore } from 'pinia';
// import { useRouter, } from 'vue-router';
import axios from 'axios';

// const router = useRouter()

export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        user: JSON.parse(localStorage.getItem('user')),
        returnUrl: null
    }),
    actions: {
        async login(email, password) {
            let user = null;
                console.log("szarlotka");
            try {
                const response = await axios.post('http://localhost:8000/login', {
                    email,
                    password
                });
                user = await response.data;
            } catch (err) {
                // error.value = err.response ? err.response.data.message : 'Error during login';
                console.log(err);
            }
            console.log(await user);
            // update pinia state
            this.user = await user;

            // store user details and jwt in local storage to keep user logged in between page refreshes
            localStorage.setItem('user', JSON.stringify(user));

            // redirect to previous url or default to home page
            // router.push('/');
        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');
            // router.push('/login');
        }
    }
});