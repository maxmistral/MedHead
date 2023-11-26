<template>
  <div class="home-page container">
    <h1>Bienvenue sur l'application MedHead</h1>
    <p>Ceci est la page d'accueil de votre application de gestion des interventions d'urgence, conçue pour améliorer l'efficacité et la rapidité des traitements médicaux en situation critique.</p>

    <h2>Caractéristiques Principales</h2>
    <ul>
      <li><strong>Gestion des Patients :</strong> Enregistrez et gérez les informations des patients, y compris leurs besoins spécifiques en matière de soins de santé.</li>
      <li><strong>Allocation des Lits d'Hôpital :</strong> Trouvez rapidement les lits disponibles dans les hôpitaux les plus proches en fonction des spécialisations requises.</li>
      <li><strong>Cartographie Intégrée :</strong> Utilisez les cartes pour localiser les patients et les hôpitaux, optimisant ainsi les temps de réponse en situation d'urgence.</li>
      <li><strong>Suivi des Interventions :</strong> Suivez en temps réel le statut des interventions d'urgence et des hospitalisations.</li>
    </ul>

    <h2>Comment Commencer ?</h2>
    <p>Commencez par naviguer dans le menu pour accéder aux différentes fonctionnalités de l'application. Vous pouvez ajouter, visualiser et gérer les informations des patients, consulter les hôpitaux et leurs disponibilités, et accéder à des outils de cartographie pour planifier les itinéraires d'urgence.</p>

    <h2>Autres points</h2>
    <p>Une page d'ETAT proposant également un stress test permet de valider le bon fonctionnement et le respect des KPI fixés dans les spécifications de l'API (support de 800 requêtes par secondes). Ce test à été exclu des tests unitaires Backend car ne permet pas d'avoir une idée réelle des délais de réponses de l'API (non considération du délais de communication entre le back-end et front-end).</p>
    <h2>Crédits</h2>
    <p>Application développée par M.B. dans le cadre du projet 11 MedHead d'OpenClassrooms.</p>
  </div>
</template>

<script>
export default {
  name: 'HomePage'
}
</script>

<style scoped>
.home-page {
  margin: 20px;
  line-height: 1.6;
}

.home-page h1 {
  color: #2c3e50;
}

.home-page h2 {
  color: #34495e;
}

.home-page ul {
  list-style-type: none;
  padding-left: 20px;
}

.home-page ul li {
  margin-bottom: 10px;
}

.home-page ul li::before {
  content: '✔️ ';
  color: #27ae60;
}
</style>
