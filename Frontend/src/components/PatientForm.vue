<template>
    <div class="container">
      <h2>{{ patient ? 'Modifier un Patient' : 'Ajouter un Patient' }}</h2>
      <b-form @submit.prevent="handleSubmit">
        <b-form-group label="Nom">
          <b-form-input v-model="patientData.nom" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Prénom">
          <b-form-input v-model="patientData.prenom" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Nom de Jeune Fille">
          <b-form-input v-model="patientData.nomJF"></b-form-input>
        </b-form-group>
  
        <b-form-group label="Date de Naissance">
          <b-form-input v-model="patientData.dateNaissance" type="date" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Adresse">
          <b-form-input v-model="patientData.adresse" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Ville">
          <b-form-input v-model="patientData.ville" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Code Postal">
          <b-form-input v-model="patientData.codePostal" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Numéro INSEE">
          <b-form-input v-model="patientData.numINSEE" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Spécialité Requise">
          <b-form-select v-model="patientData.specialiteRequise" :options="specialisationsOptions"></b-form-select>
        </b-form-group>
  
        <b-button type="submit" variant="primary">Sauvegarder</b-button>
        <b-button variant="secondary" @click="cancel">Annuler</b-button>
      </b-form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    props: {
      patient: Object,
    },
    data() {
      return {
        patientData: this.patient || {
          nom: '',
          prenom: '',
          nomJF: '',
          dateNaissance: '',
          adresse: '',
          ville: '',
          codePostal: '',
          numINSEE: '',
          hopitalActuel: null,
          specialiteRequise: null,
        },
        specialisations: [],
      };
    },
    methods: {
      fetchSpecialisations() {
        axios.get('http://localhost:8080/api/specialisation')
          .then(response => {
            this.specialisations = response.data.map(spec => ({
              value: spec.id,
              text: spec.libelle,
            }));
          })
          .catch(error => console.error('Erreur :', error));
      },
      handleSubmit() {
        const url = this.patient 
          ? `http://localhost:8080/api/patient/${this.patient.id}`
          : 'http://localhost:8080/api/patient';
  
        axios({
          method: this.patient ? 'put' : 'post',
          url: url,
          data: this.patientData,
        })
        .then(() => {
          this.$emit('saved'); // On émet l'événement saved
        })
        .catch(error => console.error('Erreur :', error));
      },
      cancel() {
        this.$emit('cancelled'); // De même pour l'événement cancelled
      },
    },
    mounted() {
      this.fetchSpecialisations();
    },
    computed: {
      specialisationsOptions() {
        return this.specialisations;
      },
    },
  };
  </script>
  
  <style scoped>
  .btn {
    margin-right: 10px;
    margin-bottom: 10px;
  }
  </style>
  