<template>
  <div class="stress-test-page">
    <h1>État des Endpoints</h1>
    <b-table striped hover :items="endpoints" :fields="fields">
      <template #cell(status)="data">
        <b-badge :variant="data.item.online ? 'success' : 'danger'">
          {{ data.item.online ? 'Online' : 'Offline' }}
        </b-badge>
      </template>
      <template #cell(averageResponseTime)="data">
        {{ data.item.averageResponseTime.toFixed(2) }} ms
      </template>
    </b-table>

    <h1>Test de Stress de l'API</h1>
    <p>Tester la réactivité de l'API avec 800 requêtes simultanées.</p>
    <b-button variant="primary" @click="performStressTest">
      Lancer le Test de Stress
    </b-button>

    <div v-if="testResults">
      <h2>Résultats du Test</h2>
      <p>Temps de réponse moyen : {{ averageResponseTime.toFixed(2) }} ms</p>
    </div>

    <div class="test-console">
      <h3>Console de Test</h3>
      <div v-for="log in testLogs" :key="log.id" class="console-log">
        {{ log.message }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { BButton, BBadge, BTable } from 'bootstrap-vue-3';

export default {
  name: 'StressTestPage',
  components: {
    BButton,
    BBadge,
    BTable,
  },
  data() {
    return {
      endpoints: [
        { url: 'http://localhost:8080/api/patient', online: false, averageResponseTime: 0 },
        { url: 'http://localhost:8080/api/hospital', online: false, averageResponseTime: 0 },
        { url: 'http://localhost:8080/api/specialisation', online: false, averageResponseTime: 0 },
        { url: 'http://localhost:8080/api/emergency/getdisponibility/1', online: false, averageResponseTime: 0 },
      ],
      fields: [
        { key: 'status', label: 'Statut' },
        { key: 'url', label: 'URL' },
        { key: 'averageResponseTime', label: 'Temps de Réponse Moyen' },
      ],
      testResults: null,
      averageResponseTime: 0,
    };
  },
  mounted() {
    this.checkEndpoints();
  },
  methods: {
    async checkEndpoints() {
      for (let endpoint of this.endpoints) {
        try {
          const startTime = performance.now();
          await axios.get(endpoint.url);
          const endTime = performance.now();
          endpoint.online = true;
          endpoint.averageResponseTime = endTime - startTime;
        } catch (error) {
          endpoint.online = false;
          console.error(`Erreur avec l'endpoint ${endpoint.url}:`, error);
        }
      }
    },
    async performStressTest() {
        this.testLogs = [];
      this.testLogs.push({ id: Date.now(), message: `[${new Date().toLocaleTimeString()}] Début de l'exécution des tests...` });

    try {
      const response = await axios.get('http://localhost:8080/api/patient');
      const patients = response.data;

      // On vérifie si la liste des patients est vide
      if (patients.length === 0) {
        alert("Aucun patient trouvé pour le test de stress.");
        return;
      }

      const totalRequests = 800;
      const requestsPerPatient = Math.ceil(totalRequests / patients.length);
      
      let totalResponseTime = 0;
      let completedRequests = 0;

      const startTime = performance.now();
      let initialDelay = 0;
      await Promise.all(
          patients.flatMap(patient => 
            Array.from({ length: requestsPerPatient }).map(async () => {
              const requestStartTime = performance.now();
              await axios.get(`http://localhost:8080/api/emergency/matchHospital/${patient.id}`);
              const requestEndTime = performance.now();
              const requestDuration = requestEndTime - requestStartTime - initialDelay;
              if(completedRequests == 0) {
                initialDelay = requestStartTime - startTime;
              }

              this.testLogs.push({ id: Date.now(), message: `[${new Date().toLocaleTimeString()}] Requête pour patient ID ${patient.id}, temps de réponse: ${requestDuration.toFixed(2)} ms` });
              completedRequests++;
            })
          )
        );
      const endTime = performance.now();

      totalResponseTime = endTime - startTime - initialDelay;
      this.averageResponseTime = totalResponseTime / completedRequests;
      this.testResults = true;
    } catch (error) {
      console.error('Erreur lors du test de stress:', error);
      this.testResults = false;
    }
  },
  },
};
</script>

<style scoped>
  .stress-test-page {
    margin: 20px;
  }

  .endpoint-status {
  margin-bottom: 10px;
}

.test-console {
  margin-top: 20px;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  padding: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.console-log {
  font-family: monospace;
}
</style>
