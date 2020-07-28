<template>
    <div>
        <b-card title="Edit Airline" style="max-width: 30rem;" class="ml-auto mr-auto mt-3">

            <b-form @submit.prevent="handleSubmitEditAirline">
                <b-form-group id="input-group-airline-name" label="Name:" label-for="input-airline-name">
                    <b-form-input id="input-airline-name" v-model="airline.name" :state="nameValidation"
                                  class="form-control" type="text" required placeholder="Enter airline name"></b-form-input>
                    <b-form-invalid-feedback :state="nameValidation">
                        Name must be 2-20 characters long.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-airline-link" label="Link:" label-for="input-airline-link">
                    <b-form-input id="input-airline-link" v-model="airline.link" :state="linkValidation"
                                  class="form-control" type="text" required placeholder="Enter airline link"></b-form-input>
                    <b-form-invalid-feedback :state="linkValidation">
                        Link must be valid ("http://... od https://...).
                    </b-form-invalid-feedback>
                </b-form-group>

                <div class="text-center">
                    <b-button @click="cancel" variant="outline-secondary" class="align-self-center mr-1">Cancel</b-button>
                    <b-button type="submit" variant="primary" class="align-self-center">Submit</b-button>
                </div>
            </b-form>

            <br v-if="showDismissibleAlert">

            <b-alert v-model="showDismissibleAlert" :variant="successful ? 'success' : 'danger'" dismissible>
                {{message}}
            </b-alert>

        </b-card>
    </div>
</template>

<script>
    import Airline from "@/models/airline"
    import AirlineService from '../../services/airline.service';

    export default {
        name: "EditAirline",
        data() {
            return {
                airline: new Airline(null, null),
                airline_ID: null,
                successful: false,
                message: '',
                showDismissibleAlert: false
            }
        },
        computed: {
            nameValidation() {
                let letters = /^[0-9a-zA-Z ]+$/
                if (!this.airline.name) return null
                if (this.airline.name.length > 1 && this.airline.name.length < 21 && this.airline.name.match(letters)) return true
                return false
            },
            linkValidation() {
                if (!this.airline.link) return null
                try {
                    new URL(this.airline.link)
                } catch (_) {
                    return false
                }
                return true
            }
        },
        methods: {
            cancel() {
                this.$router.back()
            },
            handleSubmitEditAirline() {
                this.message = '';
                if (this.nameValidation && this.linkValidation) {
                    AirlineService.postEditAirline(this.airline, this.airline_ID).then(
                        response => {
                            this.message = response.data.message;
                            this.successful = true;
                            this.$router.back()
                        },
                        error => {
                            this.message = error.response.data.message
                            this.showDismissibleAlert = true
                            this.successful = false;
                        }
                    )
                }
            },
            fetchCurrentAirline(airline_id) {
              AirlineService.getAirlineById(airline_id).then(
                  response => {
                      this.airline_ID = response.data.id
                      this.airline.name = response.data.name
                      this.airline.link = response.data.link
                  },
                  error => {
                      this.message = error.response.data.message
                      this.showDismissibleAlert = true
                      this.successful = false;
                  }
              )
            },
            currentUser() {
                return this.$store.state.auth.user;
            },
            isAdminLoggedIn() {
                if (this.currentUser() && this.currentUser().type) {
                    if (this.currentUser().type === 'ADMIN') {
                        return true;
                    }
                }
                return false;
            }
        },
        mounted() {
            if (!this.isAdminLoggedIn()) {
                this.$router.push('/');
            }
            this.fetchCurrentAirline(this.$route.params.ID)
        }
    }
</script>

<style scoped>

</style>