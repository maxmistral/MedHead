<template>
  <div class="container">
    <h2>Gestion des Hôpitaux</h2>
    <!-- Bouton ajouter -->
    <b-button @click="openAddHospital" variant="primary">Ajouter</b-button>

    <!-- Tableau Bootstrap pour l'affichage des hôpitaux -->
    <b-table striped hover :items="hospitals" :fields="fields">
      <!-- Colonne Spécialisations personnalisée -->
      <template #cell(specialisations)="data">
        <div class="row">
          <div
            class="col"
            v-for="(part, index) in dividedSpecialisations(data.item.specialisations)"
            :key="index"
          >
            <ul>
              <li v-for="specialisation in part" :key="specialisation.id">
                {{ specialisation.libelle }}
              </li>
            </ul>
          </div>
        </div>
      </template>
      <!-- Slot éditer -->
      <template #cell(edit)="data">
        <b-button variant="warning" @click="openEditHospital(data.item)">
          Éditer
          </b-button>
      </template>
      <!-- Slot supprimer -->
      <template #cell(delete)="data">
        <b-button variant="danger" @click="deleteHospital(data.item.id)">
          Supprimer
        </b-button>
      </template>
      <template #cell(googleMaps)="data">
        <b-button @click="openGoogleMaps(data.item)" variant="outline-primary"
          >Voir sur Maps</b-button
        >
      </template>
    </b-table>

    <div
      v-if="showHospitalForm"
      class="modal-overlay"
      @click.self="closeHospitalForm"
    >
      <div class="modal-content">
        <hospital-form
          :hospital="editingHospital"
          @saved="handleSave"
          @cancelled="closeHospitalForm">
        </hospital-form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import HospitalForm from './HospitalForm.vue';

export default {
  components: {
    HospitalForm,
  },
  data() {
    return {
      hospitals: [],
      editingHospital: null,
      showHospitalForm: false,
    };
  },
  methods: {
    fetchHospitals() {
      axios.get('http://localhost:8080/api/hospital')
        .then(response => {
          this.hospitals = response.data;
        })
        .catch(error => {
          console.error('Erreur :', error);
        });
    },
    openAddHospital() {
      this.editingHospital = null; // Pas d'hôpital pour un ajout
      this.showHospitalForm = true;
    },
    openEditHospital(hospital) {
      this.editingHospital = hospital; // Hôpital à éditer
      this.showHospitalForm = true;
    },
    closeHospitalForm() {
      this.showHospitalForm = false;
    },
    deleteHospital(hospitalId) {
      axios.delete(`http://localhost:8080/api/hospital/${hospitalId}`)
        .then(() => {
          this.fetchHospitals();
        })
        .catch(error => {
          console.error('Erreur lors de la suppression de l\'hôpital:', error);
        });
    },
    openGoogleMaps(hospital) {
    const address = `${hospital.adresse}, ${hospital.ville}, ${hospital.codePostal}`;
    const googleMapsUrl = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(address)}`;
    window.open(googleMapsUrl, '_blank');
    },
    handleSave() {
      this.showHospitalForm = false;
      this.fetchHospitals();
    },
  },
  mounted() {
    this.fetchHospitals();
  },
  computed: {
    dividedSpecialisations() {
      return (specialisations) => {
        const total = specialisations.length;
        const partSize = Math.ceil(total / 4); // 4 colo,nnes pour réduire la taille des lignes du tableau
        const parts = [];
        for (let i = 0; i < total; i += partSize) {
          parts.push(specialisations.slice(i, i + partSize));
        }
        return parts;
      };
    },
    fields() {
      return [
        {
          key: 'nom',
          label: 'Nom',
          sortable: true,
        },
        {
          key: 'adresse',
          label: 'Adresse',
          sortable: true,
        },
        {
          key: 'codePostal',
          label: 'Code Postal',
          sortable: true,
        },
        {
          key: 'litsDisponibles',
          label: 'Lits Disponibles',
          sortable: true,
        },
        {
          key: 'ville',
          label: 'Ville',
          sortable: true,
        },
        {
          key: 'specialisations',
          label: 'Specialisations',
          sortable: true,
        },
        {
          key: 'edit',
          label: 'Éditer',
          sortable: false, // On désactive le tri
        },
        {
          key: 'delete',
          label: 'Supprimer',
          sortable: false,
        },
        { key: 'googleMaps',
         label: 'Voir sur Maps',
         sortable: false,
        },
      ];
    },
  },
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 5px;
  max-width: 800px;
  max-height: 80%;
  overflow-y: auto;
}
</style>
