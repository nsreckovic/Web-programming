<template>
    <div>
        <b-card title="New Flight Instance" style="max-width: 30rem;" class="ml-auto mr-auto mt-3">

            <b-form @submit.prevent="handleSubmitAddFlightInstance">
                <b-form-group id="input-group-flightInstance-id" label="Flight ID:" label-for="input-flightInstance-flight-id">
                    <b-form-select id="input-flightInstance-date" v-model="flightInstance.flight_ID" :plain="true"
                                   :options="flights" class="form-control" required>
                    </b-form-select>
                </b-form-group>

                <b-form-group id="input-group-flightInstance-date" label="Date:" label-for="input-flightInstance-date">
                    <b-form-datepicker id="input-flightInstance-date" v-model="flightInstance.date"
                                       :state="dateValidation" required :min="min" :max="max"></b-form-datepicker>
                    <b-form-invalid-feedback :state="dateValidation">
                        Date must be in future.
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
    import FlightInstance from "@/models/flightInstance"
    import FlightInstanceService from '../../services/flightInstance.service';
    import FlightService from '../../services/flight.service';

    export default {
        name: "NewFlight",
        data() {
            const now = new Date()
            const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
            const minDate = new Date(today)

            const maxDate = new Date(today)
            maxDate.setMonth(maxDate.getMonth() + 6)
            return {
                min: minDate,
                max: maxDate,
                flights: [{
                    text: 'Select a flight',
                    value: null,
                }],
                flightInstance: new FlightInstance(null, null),
                successful: false,
                message: '',
                showDismissibleAlert: false
            }
        },
        computed: {
            dateValidation() {
                if (!this.flightInstance.date) return null
                var current_date = new Date()
                var selected_date = new Date(this.flightInstance.date)

                if (selected_date < current_date) return false;
                return true;
            }
        },
        methods: {
            handleSubmitAddFlightInstance() {
                this.message = ''
                if (!this.flightInstance.date) {
                    this.message = 'Error: Date must be selected!'
                    this.showDismissibleAlert = true
                    return
                }
                if (this.dateValidation) {
                    FlightInstanceService.postAddFlightInstance(this.flightInstance).then(
                        response => {
                            this.message = response.data.message;
                            this.successful = true;
                            this.$router.push('/flightInstances');
                        },
                        error => {
                            if (error.response) this.message = error.response.data.message || error.message
                            else this.message = error.toString()
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

            FlightService.getAllFlights().then(
                response => {
                    response.data.forEach((flight) => {
                        this.flights.push({
                            text: flight.flight_ID + ' ' + flight.airline + ' ' + flight.departure_airport + ' -> ' + flight.arrival_airport,
                            value: flight.flight_ID})
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