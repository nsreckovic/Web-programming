<template>
    <div>
        <b-card title="New Flight" style="max-width: 30rem;" class="ml-auto mr-auto mt-3">

            <b-form @submit.prevent="handleSubmitAddFlight">
                <b-form-group id="input-group-flight-id" label="ID:" label-for="input-flight-id">
                    <b-form-input id="input-flight-id" v-model="flight.id" :state="idValidation"
                                  class="form-control" type="text" required placeholder="Enter flight ID"></b-form-input>
                    <b-form-invalid-feedback :state="idValidation">
                        Airport ID must be 2-20 characters long.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-flight-airline" label="Airline:" label-for="input-flight-airline">
                    <b-form-select id="input-flight-airline" v-model="flight.airline" :plain="true"
                                   :options="airlines" class="form-control" required>
                    </b-form-select>
                </b-form-group>

                <b-form-group id="input-group-flight-departure-airport-ID" label="Departure airport (ID):" label-for="input-flight-departure-airport-ID">
                    <b-form-select id="input-flight-departure-airport-ID" v-model="flight.departure_airport_ID" :state="airportsValidation"
                                   :plain="true" :options="departure_airports" class="form-control" required>
                    </b-form-select>
                    <b-form-invalid-feedback :state="airportsValidation">
                        Departure and arrival airport must be different.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-flight-arrival-airport-ID" label="Arrival airport (ID):" label-for="input-flight-arrival-airport-ID">
                    <b-form-select id="input-flight-arrival-airport-ID" v-model="flight.arrival_airport_ID" :state="airportsValidation"
                                   :plain="true" :options="arrival_airports" class="form-control" required>
                    </b-form-select>
                    <b-form-invalid-feedback :state="airportsValidation">
                        Departure and arrival airport must be different.
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
    import Flight from "@/models/flight"
    import FlightService from '../../services/flight.service';
    import AirlineService from '../../services/airline.service';
    import AirportService from '../../services/airport.service';

    export default {
        name: "NewFlight",
        data() {
            return {
                airlines: [{
                    text: 'Select an airline',
                    value: null,
                }],
                departure_airports: [{
                    text: 'Select a departure airport',
                    value: null,
                }],
                arrival_airports: [{
                    text: 'Select an arrival airport',
                    value: null,
                }],
                flight: new Flight(null, null, null, null),
                successful: false,
                message: '',
                showDismissibleAlert: false
            }
        },
        computed: {
            idValidation() {
                let letters = /^[0-9a-zA-Z]+$/
                if (!this.flight.id) return null
                if (this.flight.id.length > 1 && this.flight.id.length < 21 && this.flight.id.match(letters)) return true
                return false
            },
            airportsValidation() {
                if (!this.flight.departure_airport_ID || !this.flight.arrival_airport_ID) return null
                if (this.flight.departure_airport_ID === this.flight.arrival_airport_ID) return false
                return true
            }
        },
        methods: {
            handleSubmitAddFlight() {
                this.message = '';
                if (this.idValidation && this.airportsValidation) {
                    this.flight.id = this.flight.id.toUpperCase()
                    FlightService.postAddFlight(this.flight).then(
                        response => {
                            this.message = response.data.message;
                            this.successful = true;
                            this.$router.push('/flights');
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
            AirlineService.getAllAirlines().then(
                response => {
                    response.data.forEach((airline) => {
                        this.airlines.push({
                            text: airline.name,
                            value: airline.name
                        })
                    })
                },
                error => {
                    this.message = error.response.data.message
                    this.showDismissibleAlert = true
                    this.successful = false;
                }
            )
            AirportService.getAllAirports().then(
                response => {
                    response.data.forEach((airport) => {
                        this.departure_airports.push({text:airport.name, value:airport.id})
                        this.arrival_airports.push({text:airport.name, value:airport.id})
                    })
                },
                error => {
                    this.message = error.response.data.message
                    this.showDismissibleAlert = true
                    this.successful = false;
                }
            )


        }
    }
</script>

<style scoped>

</style>