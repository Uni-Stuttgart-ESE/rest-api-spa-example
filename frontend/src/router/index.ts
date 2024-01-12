import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from "@/views/AboutView.vue";
import CatsView from "@/views/CatsView.vue";
import CatsCreateView from '@/views/CatsCreateView.vue';
import CatsDetailsView from '@/views/CatsDetailsView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView
    },
    {
      path: '/cats',
      name: 'cats',
      component: CatsView
    },
    {
      path: '/create-cat',
      name: 'create',
      component: CatsCreateView
    },
    {
      path: '/cats/:id',
      name: 'details',
      component: CatsDetailsView
    },

  ]
})

export default router
