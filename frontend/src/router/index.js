import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ListenView from '../views/ListenView.vue'
import ListeDetailView from '../views/ListeDetailView.vue'
import MaerkteView from '../views/MaerkteView.vue'
import MarktAnsichtView from '../views/MarktAnsichtView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/listen', name: 'listen', component: ListenView },
    { path: '/listen/:id', name: 'liste', component: ListeDetailView },
    { path: '/maerkte', name: 'maerkte', component: MaerkteView },
    { path: '/maerkte/:id', name: 'markt-ansicht', component: MarktAnsichtView },
  ],
})

export default router
