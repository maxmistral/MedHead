<template>
  <div class="container">
    <h2>Gestion des Patients</h2>
    <!-- Bouton ajouter -->
    <b-button @click="openAddPatient" variant="primary">Ajouter</b-button>

    <!-- Tableau Bootstrap pour l'affichage des patients -->

    <b-table striped hover :items="patients" :fields="fields">
      <template #cell(specialiteRequise)="data">
        {{ getSpecialiteLibelle(data.item.specialiteRequise) }}
      </template>
      <template #cell(hopitalActuel)="data">
        {{ getHopitalNom(data.item.hopitalActuel) }}
      </template>
      <!-- Slot éditer -->
      <template #cell(edit)="data">
        <b-button variant="warning" @click="openEditPatient(data.item)">
          Éditer
          </b-button>
      </template>
      <!-- Slot supprimer -->
      <template #cell(delete)="data">
        <b-button variant="danger" @click="deletePatient(data.item.id)"
          >Supprimer
        </b-button>
      </template>
      <template #cell(googleMaps)="data">
        <b-button @click="openGoogleMaps(data.item)" variant="outline-primary">
          Voir sur Maps
          </b-button>
      </template>
      <template #cell(findHospital)="data">
        <b-button @click="openFindHospitalModal(data.item)" variant="outline-primary">
          Trouver un hôpital
        </b-button>
    </template>
    </b-table>
    <div v-if="showPatientForm" class="modal-overlay" @click.self="closePatientForm">
      <div class="modal-content">
        <patient-form
          :patient="editingPatient"
          @saved="handleSave"
          @cancelled="closePatientForm"
        ></patient-form>
      </div>
    </div>
    <div v-if="showHospitalModal" class="modal-overlay" @click.self="closeHospitalModal">
        <div class="modal-content">
          <div @click="closeHospitalModal" class="modal-close-icon">× Fermer</div>
          <h2>Trajet vers {{ selectedHospital?.nom }}</h2>
          <p><b>L'hopital suggéré le plus proche est {{ selectedHospital?.nom }} qui possède la spécialité {{getSpecialiteLibelle(patient.specialiteRequise) }} avec {{ selectedHospital?.litsDisponibles }} lits de disponibles.</b></p>
          <p>N'ayant pas de clé d'API (payante), il s'agit d'une implémentation démo et fonctionnelle dans la mesure où il s'agit juste de renseigner la clé pour avoir un visuel IFrame du trajet.</p> 
          <iframe v-if="googleMapsAPIUrl" :src="googleMapsAPIUrl" width="100%" height="450" frameborder="0" allowfullscreen loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe> 
          <b-button @click="openGoogleMapsTravel()" variant="outline-primary">
              Ouvrir dans Google Maps
            </b-button>
            <b-button v-if="patient.hopitalActuel == -1" @click="associatePatientToHospital()" variant="primary">
              Associer le patient à l'établissement suggéré
            </b-button>
            <b-button v-if="patient.hopitalActuel !== -1" @click="associatePatientToHospital(true)" variant="warning">
              Dissocier le patient de l'hôpital courant
            </b-button>

        </div>
      </div>
  </div>
</template>

<script>
import axios from 'axios';
import PatientForm from './PatientForm.vue';

export default {
  components: {
    PatientForm,
  },
  data() {
    return {
      // Données du composant
      patients: [],
      specialisations: [],
      hopitaux: [],
      editingPatient: null,
      showPatientForm: false,
      showHospitalModal: false,
      selectedHospital: null,
      patient: null,
      googleMapsAPIUrl: '',
      googleMapsUrl: '',
    };
  },

  // ======== Méthodes de cycle de vie ========
  mounted() {
    this.fetchPatients();
    this.fetchSpecialisations();
    this.fetchHopitaux();
  },

  // ======== Méthodes relatives au CRUD des patients ========
  methods: {
    // Fetching data
    fetchPatients() {
      axios.get('http://localhost:8080/api/patient')
        .then(response => {
          this.patients = response.data;
        })
        .catch(error => {
          console.error('Erreur :', error);
        });
    },
    fetchSpecialisations() {
      axios.get('http://localhost:8080/api/specialisation')
        .then(response => {
          this.specialisations = response.data;
        })
        .catch(error => {
          console.error('Erreur :', error);
        });
    },
    fetchHopitaux() {
      axios.get('http://localhost:8080/api/hospital')
        .then(response => {
          this.hopitaux = response.data;
        })
        .catch(error => {
          console.error('Erreur :', error);
        });
    },

    // CRUD operations
    openAddPatient() {
      this.editingPatient = null;
      this.showPatientForm = true;
    },
    openEditPatient(patient) {
      this.editingPatient = patient;
      this.showPatientForm = true;
    },
    deletePatient(patientId) {
      const patientToDelete = this.patients.find(p => p.id === patientId);
      axios.delete(`http://localhost:8080/api/patient/${patientId}`)
        .then(() => {
          // Si le patient avait un hôpital associé, on mets à jour le nombre de lits
          if (patientToDelete && patientToDelete.hopitalActuel !== -1) {
            const hospital = this.hopitaux.find(h => h.id === patientToDelete.hopitalActuel);
            if (hospital) {
              this.editLitsDisponibles(hospital, hospital.litsDisponibles + 1);
            }
          }
          this.fetchPatients();
        })
        .catch(error => {
          console.error('Erreur lors de la suppression du patient:', error);
        });
    },
    handleSave() {
      this.showPatientForm = false;
      this.fetchPatients();
      this.fetchHopitaux();
    },

    // ======== Méthodes relatives à l'interface utilisateur ========
    openGoogleMaps(patient) {
      const address = `${patient.adresse}, ${patient.ville}, ${patient.codePostal}`;
      const googleMapsUrl = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(address)}`;
      window.open(googleMapsUrl, '_blank');
    },
    openGoogleMapsTravel() {
      window.open(this.googleMapsUrl, '_blank');
    },
    closePatientForm() {
      this.showPatientForm = false;
    },
    closeHospitalModal() {
      this.showHospitalModal = false;
    },

    // ======== Méthodes relatives aux opérations diverses relatives aux hopitaux ========
    openFindHospitalModal(patient) {
      axios.get(`http://localhost:8080/api/emergency/matchHospital/${patient.id}`)
        .then(response => {
          this.selectedHospital = response.data;
          this.patient = patient;
          const origin = `${patient.adresse}, ${patient.ville}, ${patient.codePostal}`;
          const destination = `${this.selectedHospital.adresse}, ${this.selectedHospital.ville}, ${this.selectedHospital.codePostal}`;
          this.googleMapsAPIUrl = `https://www.google.com/maps/embed/v1/directions?key=&origin=${encodeURIComponent(origin)}&destination=${encodeURIComponent(destination)}`;
          this.googleMapsUrl = `https://www.google.com/maps/dir/?api=1&origin=${encodeURIComponent(origin)}&destination=${encodeURIComponent(destination)}&travelmode=driving`;
          this.showHospitalModal = true;
        })
        .catch(error => {
          console.error('Erreur :', error);
        });
    },
    associatePatientToHospital(dissociate = false) {
      if (dissociate) {
        if(this.patient.hopitalActuel != -1) {
          this.patient.hopitalActuel = -1;
          this.editLitsDisponibles(this.selectedHospital, this.selectedHospital.litsDisponibles + 1);
        }
      } else {
        this.patient.hopitalActuel = this.selectedHospital.id;
        this.editLitsDisponibles(this.selectedHospital, this.selectedHospital.litsDisponibles - 1);
      }

      axios.put(`http://localhost:8080/api/patient/${this.patient.id}`, this.patient)
        .then(() => {
          this.showHospitalModal = false;
          this.fetchPatients();
          this.fetchHopitaux();
        })
        .catch(error => {
          console.error("Erreur lors de l'association du patient à l'établissement :", error);
        });
    },
    editLitsDisponibles(hospital, litsDisponibles) {
      hospital.litsDisponibles = litsDisponibles;
      axios.put(`http://localhost:8080/api/hospital/${hospital.id}`, hospital)
        .then(() => {
          this.showHospitalModal = false;
          this.fetchHopitaux();
        })
        .catch(error => {
          console.error("Erreur lors de la mise à jour des lits disponibles :", error);
        });
    },

    // ======== Méthodes d'aide ========
    getSpecialiteLibelle(specialiteId) {
      const specialite = this.specialisations.find(s => s.id === specialiteId);
      return specialite ? specialite.libelle : 'Inconnue';
    },
    getHopitalNom(hopitalId) {
      if (hopitalId === -1) {
        return 'Aucun';
      }
      const hopital = this.hopitaux.find(h => h.id === hopitalId);
      return hopital ? hopital.nom : 'Autre';
    },
  },

  // ======== Propriétés calculées ========
  computed: {
    fields() {
      return [
        { key: 'nom', label: 'Nom', sortable: true },
        { key: 'prenom', label: 'Prénom', sortable: true },
        { key: 'nomJF', label: 'Nom de Jeune Fille', sortable: true },
        { key: 'dateNaissance', label: 'Date de Naissance', sortable: true },
        { key: 'adresse', label: 'Adresse', sortable: true },
        { key: 'ville', label: 'Ville', sortable: true },
        { key: 'codePostal', label: 'Code Postal', sortable: true },
        { key: 'numINSEE', label: 'Numéro INSEE', sortable: true },
        { key: 'hopitalActuel', label: 'Hôpital Actuel', sortable: true },
        { key: 'specialiteRequise', label: 'Spécialité Requise', sortable: true },
        { key: 'edit', label: 'Éditer', sortable: false },
        { key: 'delete', label: 'Supprimer', sortable: false },
        { key: 'googleMaps', label: 'Voir sur Maps', sortable: false, },
        { key: 'findHospital', label: 'Trouver un hôpital' },
      ];
    },
  }
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


.modal-content .btn {
    margin-right: 10px;
    margin-bottom: 10px;
  }

  .modal-close-icon {
    position: absolute;
    top: 10px;
    right: 10px;
    cursor: pointer;
  }
</style>
