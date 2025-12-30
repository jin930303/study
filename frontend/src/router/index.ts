import {createRouter, createWebHistory} from 'vue-router';
import HomeView from '../views/HomeView.vue';

const router = createRouter({
    history : createWebHistory(import.meta.env.BASE_URL),
    routes : [
        {
            path : "/",
            name : "home",
            component : HomeView
        },
        {
            path : "/login",
            name : "login",
            component : () => import('../views/login/LoginView.vue')
        },
        {
            path : "/member/Join",
            name : "join",
            component : () => import('../views/member/JoinView.vue')
        }
    ]
});

export default router;