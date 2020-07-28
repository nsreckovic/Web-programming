<template>
    <div>
        <b-card title="New Airline" style="max-width: 30rem;" class="ml-auto mr-auto mt-3">

            <b-form @submit.prevent="handleSubmitAddAirline">
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
        name: "NewAirline",
        data() {
            return {
                airline: new Airline(null, null),
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
            handleSubmitAddAirline() {
                this.message = '';
                if (this.nameValidation && this.linkValidation) {
                    AirlineService.postAddAirline(this.airline).then(
                        response => {
                            this.message = response.data.message;
                            this.successful = true;
                            this.$router.push('/airlines');
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
        }
    }
</script>

<style scoped>

</style>