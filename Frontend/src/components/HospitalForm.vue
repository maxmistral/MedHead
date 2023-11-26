<template>
    <div class="container">
      <h2>{{ hospital ? 'Modifier un Hôpital' : 'Ajouter un Hôpital' }}</h2>
      <b-form @submit.prevent="handleSubmit">
        <b-form-group label="Nom de l'hôpital">
          <b-form-input v-model="hospitalData.nom" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Adresse">
          <b-form-input v-model="hospitalData.adresse" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Code Postal">
          <b-form-input v-model="hospitalData.codePostal" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Ville">
          <b-form-input v-model="hospitalData.ville" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Lits Disponibles">
          <b-form-input v-model="hospitalData.litsDisponibles" type="number" required></b-form-input>
        </b-form-group>
  
        <b-form-group label="Sélectionnez les spécialisations">
            <div class="row">
                <div class="col" v-for="(part, index) in dividedSpecialisations" :key="index">
                <div v-for="specialisation in part" :key="specialisation.id">
                    <b-form-checkbox 
                    v-model="selectedSpecialisations" 
                    :value="specialisation.id"
                    >
                    {{ specialisation.libelle }}
                    </b-form-checkbox>
                </div>
                </div>
            </div>
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
      hospital: Object,
    },
    data() {
      return {
        hospitalData: this.hospital || {
          nom: '',
          adresse: '',
          codePostal: '',
          ville: '',
          litsDisponibles: 0,
          specialisations: [],
        },
        specialisations: [],
        selectedSpecialisations: [],
      };
    },
    methods: {
      fetchSpecialisations() {
        axios.get('http://localhost:8080/api/specialisation')
          .then(response => {
            this.specialisations = response.data;
          })
          .catch(error => console.error('Erreur :', error));
      },
      handleSubmit() {
        this.hospitalData.specialisations = this.selectedSpecialisations.map(id => 
            this.specialisations.find(spec => spec.id === id)
        );
  
        const url = this.hospital 
          ? `http://localhost:8080/api/hospital/${this.hospital.id}`
          : 'http://localhost:8080/api/hospital';
  
        axios({
          method: this.hospital ? 'put' : 'post',
          url: url,
          data: this.hospitalData,
        })
        .then(() => {
        this.$emit('saved'); // On émet l'événement saved
      })
        .catch(error => console.error('Erreur :', error));
      },
      cancel() {
        this.$emit('cancelled'); // EDe même pour l'événement cancelled
      },
    },
    mounted() {
      this.fetchSpecialisations();
      if (this.hospital) {
        this.selectedSpecialisations = this.hospital.specialisations.map(s => s.id);
      }
    },
    computed: {
        dividedSpecialisations() {
        const total = this.specialisations.length;
        const partSize = Math.ceil(total / 4); // Modification pour diviser en 4 parties
        return [
            this.specialisations.slice(0, partSize),
            this.specialisations.slice(partSize, 2 * partSize),
            this.specialisations.slice(2 * partSize, 3 * partSize),
            this.specialisations.slice(3 * partSize) // Ajout d'une quatrième partie
        ];
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