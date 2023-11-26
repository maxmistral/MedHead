import { createRouter, createWebHistory } from 'vue-router';
import HomePage from './components/HomePage.vue';
import PatientCrud from './components/PatientCrud.vue';
import HospitalCrud from './components/HospitalCrud.vue';
import StressTest from './components/StressTest.vue';

const routes = [
  { path: '/', component: HomePage },
  { path: '/patients', component: PatientCrud },
  { path: '/hospitals', component: HospitalCrud },
  { path: '/stress-test', component: StressTest },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
