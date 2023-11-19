<template>
  <div class="container mt-5">
    <h2 class="mb-4">Hôpitaux</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nom</th>
          <th>Adresse</th>
          <th>Ville</th>
          <th>Code Postal</th>
          <th>Lits Disponibles</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="hospital in hospitals" :key="hospital.id">
          <td>{{ hospital.id }}</td>
          <td>{{ hospital.nom }}</td>
          <td>{{ hospital.adresse }}</td>
          <td>{{ hospital.ville }}</td>
          <td>{{ hospital.codePostal }}</td>
          <td>{{ hospital.litsDisponibles }}</td>
        </tr>
      </tbody>
    </table>

    <h2 class="mb-4">Ajouter un hôpital</h2>
    <form @submit.prevent="addHospital">
      <!-- ... (reste du formulaire) ... -->
    </form>
    
    <h2 class="mb-4">Ajouter un patient</h2>
    <form @submit.prevent="addPatient">
      <!-- ... (reste du formulaire) ... -->
    </form>
  </div>
</template>

<script>
export default {
  name: "EmergencyResponseSystem",
  data() {
    return {
      hospitals: [],
      hospitalName: "",
      hospitalCity: "",
      hospitalBeds: "",
      patientFirstName: "",
      patientLastName: "",
      patientAddress: "",
      patientCity: "",
      patientZip: "",
      apiUsername: 'user',
      apiPassword: '14c881a9-4a31-4b71-abdf-7d7fd8d17307'
    };
  },
  methods: {
    getAuthHeaders() {
      const headers = new Headers();
      headers.set('Authorization', 'Basic ' + btoa(this.apiUsername + ":" + this.apiPassword));
      headers.set('Content-Type', 'application/json');
      return headers;
    },
    loadHospitals() {
      fetch('http://localhost:8080/api/emergency', { headers: this.getAuthHeaders() })
        .then(response => response.json())
        .then(data => {
          this.hospitals = data;
        })
        .catch(error => {
          console.error('Erreur lors du chargement des hôpitaux:', error);
        });
    },
    addHospital() {
      const hospital = {
        Nom: this.hospitalName,
        Ville: this.hospitalCity,
        LitsDisponibles: this.hospitalBeds
      };

      fetch('http://localhost:8080/api/emergency', {
        method: 'POST',
        headers: this.getAuthHeaders(),
        body: JSON.stringify(hospital)
      })
      .then(response => {
        if (response.ok) {
          alert('L\'hôpital a été ajouté avec succès.');
          this.loadHospitals();
        } else {
          throw new Error('Erreur lors de l\'ajout de l\'hôpital.');
        }
      })
      .catch(error => {
        alert('Une erreur est survenue lors de l\'ajout de l\'hôpital.');
        console.error(error);
      });
    },
    addPatient() {
      const patient = {
        Id: -1,
        Prenom: this.patientFirstName,
        Nom: this.patientLastName,
        NomJF: '',
        DateNaissance: '',
        Adresse: this.patientAddress,
        Ville: this.patientCity,
        CodePostal: this.patientZip,
        NumINSEE: '',
        Location: '',
        SpecialiteRequise: 0
      };

      fetch('http://localhost:8080/api/emergency', {
        method: 'POST',
        headers: this.getAuthHeaders(),
        body: JSON.stringify(patient)
      })
      .then(response => {
        if (response.ok) {
          alert('Le patient a été ajouté avec succès.');
          this.loadHospitals();
        } else {
          throw new Error('Erreur lors de l\'ajout du patient.');
        }
      })
      .catch(error => {
        alert('Une erreur est survenue lors de l\'ajout du patient.');
        console.error(error);
      });
    }
  },
  mounted() {
    this.loadHospitals();
  }
}
</script>

<style scoped>
/* Vos styles ici si besoin */
</style>
