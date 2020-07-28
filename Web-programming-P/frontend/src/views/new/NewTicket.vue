<template>
    <div>
        <b-card title="New Ticket" style="max-width: 30rem;" class="ml-auto mr-auto mt-3">

            <b-form @submit.prevent="handleSubmitAddTicket">
                <b-form-group id="input-group-ticket-flight-instance-id" label="Flight Instance ID:" label-for="input-ticket-flight-instance-id">
                    <b-form-select id="input-ticket-flight-instance-id" v-model="ticket.flight_instance_ID" :plain="true"
                                   :options="flightInstances" class="form-control" required>
                    </b-form-select>
                </b-form-group>

                <b-form-group id="input-group-ticket-count" label="Count:" label-for="input-ticket-count">
                    <b-form-input id="input-ticket-count" v-model="ticket.count" :state="countValidation"
                                  class="form-control" type="number" required placeholder="Enter ticket count" min="1" max="500" step="1"></b-form-input>
                    <b-form-invalid-feedback :state="countValidation">
                        Count must be grater than 0 and less than 500.
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
    import Ticket from "@/models/ticket"
    import TicketService from '@/services/ticket.service'
    import FlightInstanceService from '@/services/flightInstance.service'

    export default {
        name: "NewTicket",
        data() {
            return {
                flightInstances: [{
                    text: 'Select a flight instance',
                    value: null,
                }],
                ticket: new Ticket(null, null),
                successful: false,
                message: '',
                showDismissibleAlert: false
            }
        },
        computed: {
            countValidation() {
                if (!this.ticket.count) return null
                if (this.ticket.count < 1 || this.ticket.count > 500) return false;
                return true;
            }
        },
        methods: {
            handleSubmitAddTicket() {
                this.message = ''
                if (this.countValidation) {
                    TicketService.postAddTicket(this.ticket).then(
                        response => {
                            this.message = response.data.message;
                            this.successful = true;
                            this.$router.push('/tickets');
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

            FlightInstanceService.getAllFlightInstances().then(
                response => {
                    response.data.forEach((flightInstance) => {
                        this.flightInstances.push({
                            text: flightInstance.flight_ID + ' on ' + flightInstance.date,
                            value: flightInstance.flightInstance_ID})
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