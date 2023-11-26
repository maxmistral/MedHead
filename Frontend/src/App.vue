<template>
  <div id="app" class="d-flex">
    <b-navbar vertical class="sidebar">
      <b-nav vertical>
        <b-nav-item to="/">Accueil</b-nav-item>
        <b-nav-item to="/patients">Gestion des Patients</b-nav-item>
        <b-nav-item to="/hospitals">Gestion des Hôpitaux</b-nav-item>
        <b-nav-item to="/stress-test">Test de Stress</b-nav-item>
      </b-nav>
    </b-navbar>
    <div class="main-content flex-grow-1">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
.sidebar {
  width: 200px;
  background-color: #f0f0f0;
  height: 100vh; /* Hauteur totale de la vue */
}

.main-content {
  padding: 20px;
}
</style>
