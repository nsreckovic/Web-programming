<template>
    <b-card style="max-width: 60%;" class="ml-auto mr-auto mt-3">
        <b-row class="ml-auto mb-3">
            <b-card-title>
                <h1>Flights</h1>
            </b-card-title>
        </b-row>

        <b-alert v-model="showDismissibleAlert" :variant="variant" dismissible class="my-2">
            {{message}}
        </b-alert>

        <b-row class="ml-auto">
            <h4>All flights:</h4>
            <b-table show-empty
                     small
                     stacked="md"
                     :items="table_flights"
                     :fields="flights_fields"
                     striped
                     hover
                     class="text-center">

                <template v-slot:cell(airline)="row">
                    <a :href=row.item.airline_link target="_blank" class="text-dark">{{row.item.airline}}</a>
                </template>

                <template v-slot:cell(edit)="row">
                    <b-button size="sm" @click="editFlight(row.item)">Edit</b-button>
                </template>

                <template v-slot:cell(delete)="row">
                    <b-button size="sm" variant="danger" @click="deletePrompt(row.item, row.index, $event.target)">
                        Delete
                    </b-button>
                </template>

            </b-table>
        </b-row>

        <b-modal :id="infoModal.id" hide-footer hide-header centered size="lg" :title="infoModal.title"
                 @hide="resetInfoModal">
            <h4 align="center">Are you sure you want to delete this flight?</h4>
            <div class="text-center mt-5">
                <b-button class="mr-1" variant="outline-secondary" @click="hideModal">Cancel</b-button>
                <b-button class="mr-1" variant="danger" @click="deleteFlight">Delete</b-button>
            </div>
        </b-modal>

    </b-card>
</template>

<script>
    import FlightService from "@/services/flight.service";

    export default {
        name: "FlightsPage",
        data() {
            return {
                // Flights
                table_flights: [],
                flights_fields: [
                    {key: 'ID'},
                    {key: 'airline'},
                    {key: 'airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'departure_airport'},
                    {key: 'arrival_airport'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ],

                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',

                infoModal: {
                    id: 'delete-modal',
                    content: ''
                }
            }
        },
        methods: {
            fetchFlights() {
                this.table_flights = []
                FlightService.getAllFlights().then(
                    response => {
                        response.data.forEach((flight) => {
                            this.table_flights.push({
                                ID: flight.flight_ID,
                                airline: flight.airline,
                                airline_link: flight.airline_link,
                                departure_airport: flight.departure_airport,
                                arrival_airport: flight.arrival_airport,
                                edit: null,
                                delete: null
                            })
                        })
                    },
                    error => {
                        this.message = error.response.data.message
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            editFlight(data) {
                this.$router.push({name: 'editFlight', params: data})
            },
            resetInfoModal() {
                this.infoModal.content = null
            },
            hideModal() {
                this.$root.$emit('bv::hide::modal', this.infoModal.id)
            },
            deletePrompt(item, index, button) {
                this.infoModal.content = item
                this.$root.$emit('bv::show::modal', this.infoModal.id, button)
            },
            deleteFlight() {
                FlightService.deleteFlight(this.infoModal.content.ID).then(
                    response => {
                        if (response.status == 200) {
                            this.table_flights = []
                            this.hideModal()
                            this.fetchFlights()
                        }
                    }, error => {
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "danger"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            currentUser() {
                return this.$store.state.auth.user;
            },
        },
        computed: {
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
            if (!this.isAdminLoggedIn) {
                this.$router.push('/login');
            }
            this.fetchFlights()
        }
    }
</script>

<style scoped>

</style>