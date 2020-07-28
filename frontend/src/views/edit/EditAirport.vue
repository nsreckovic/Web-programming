<template>
    <div>
        <b-card title="Edit Airport" style="max-width: 30rem;" class="ml-auto mr-auto mt-3">

            <b-form @submit.prevent="handleSubmitEditAirport">
                <b-form-group id="input-group-airport-id" label="ID:" label-for="input-airport-id">
                    <b-form-input id="input-airport-id" v-model="airport.id" :state="idValidation"
                                  class="form-control" type="text" required placeholder="Enter airport ID"></b-form-input>
                    <b-form-invalid-feedback :state="idValidation">
                        Airport ID must be 2-20 characters long.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-airport-name" label="Name:" label-for="input-airport-name">
                    <b-form-input id="input-airport-name" v-model="airport.name" :state="nameValidation"
                                  class="form-control" type="text" required placeholder="Enter airport name"></b-form-input>
                    <b-form-invalid-feedback :state="nameValidation">
                        Airport Name must be 2-30 characters long.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-airport-place" label="Place:" label-for="input-airport-place">
                    <b-form-input id="input-airport-place" v-model="airport.place" :state="placeValidation"
                                  class="form-control" type="text" required placeholder="Enter airport place"></b-form-input>
                    <b-form-invalid-feedback :state="placeValidation">
                        Airport Place must be 2-20 characters long.
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
    import Airport from "@/models/airport";
    import AirportService from "@/services/airport.service";

    export default {
        name: "EditAirport",
        data() {
            return {
                airport: new Airport(null, null, null),
                successful: false,
                message: '',
                showDismissibleAlert: false
            }
        },
        computed: {
            idValidation() {
                let letters = /^[0-9a-zA-Z]+$/
                if (!this.airport.id) return null
                if (this.airport.id.length > 1 && this.airport.id.length < 21 && this.airport.id.match(letters)) return true
                return false
            },
            nameValidation() {
                let letters = /^[0-9a-zA-Z ]+$/
                if (!this.airport.name) return null
                if (this.airport.name.length > 1 && this.airport.name.length < 31 && this.airport.name.match(letters)) return true
                return false
            },
            placeValidation() {
                let letters = /^[0-9a-zA-Z ]+$/
                if (!this.airport.place) return null
                if (this.airport.place.length > 1 && this.airport.place.length < 21 && this.airport.place.match(letters)) return true
                return false
            }
        },
        methods: {
            cancel() {
                this.$router.back()
            },
            handleSubmitEditAirport() {
                this.message = '';
                if (this.idValidation && this.nameValidation && this.placeValidation && this.$route.params.ID) {
                    this.airport.id = this.airport.id.toUpperCase()
                    AirportService.postUpdateAirport(this.airport, this.$route.params.ID).then(
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
            this.airport.id = this.$route.params.ID
            this.airport.name = this.$route.params.name
            this.airport.place = this.$route.params.place
        }
    }
</script>

<style scoped>

</style>