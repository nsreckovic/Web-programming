<template>
    <b-card style="max-width: 75%;" class="ml-auto mr-auto mt-3">
        <b-row class="ml-auto mb-3">
            <b-card-title>
                <h1>Airlines</h1>
            </b-card-title>
        </b-row>

        <b-row class="ml-auto mb-5">
            <h4>All airlines:</h4>
            <b-table show-empty
                     small
                     stacked="md"
                     :items="table_airlines"
                     :fields="airline_fields"
                     striped
                     hover
                     class="text-center">

                <template v-slot:cell(name)="row">
                    <a :href=row.item.link target="_blank" class="text-dark">{{row.item.name}}</a>
                </template>

                <template v-slot:cell(edit)="row">
                    <b-button size="sm" @click="editAirline(row.item)">Edit</b-button>
                </template>

                <template v-slot:cell(delete)="row">
                    <b-button size="sm" variant="danger" @click="deletePrompt(row.item, row.index, $event.target)">
                        Delete
                    </b-button>
                </template>

            </b-table>
        </b-row>

        <b-row class="ml-auto mb-4">
            <b-col cols="2" class="p-0 m-0"><h4>All ticket for airline:</h4></b-col>
            <b-col cols="1" class="p-0 m-0">
                <b-form-select id="new-res-filter-airline" v-model="airline" :plain="true"
                               :options="airlines" class="form-control pr-0" @change="getTicketsForAirline">
                </b-form-select>
            </b-col>
        </b-row>

        <b-alert v-model="showDismissibleAlert" :variant="variant" dismissible class="mb-4">
            {{message}}
        </b-alert>

        <b-table show-empty
                 small
                 stacked="md"
                 :items="tickets"
                 :fields="fields"
                 striped
                 hover
                 class="text-center">

            <template v-slot:cell(airline)="row">
                <a :href=row.item.airline_link target="_blank" class="text-dark">{{row.item.airline}}</a>
            </template>

        </b-table>

        <b-modal :id="infoModal.id" hide-footer hide-header centered size="lg" :title="infoModal.title"
                 @hide="resetInfoModal">
            <h4 align="center">Are you sure you want to delete this airline?</h4>
            <div class="text-center mt-5">
                <b-button class="mr-1" variant="outline-secondary" @click="hideModal">Cancel</b-button>
                <b-button class="mr-1" variant="danger" @click="deleteAirline">Delete</b-button>
            </div>
        </b-modal>
    </b-card>
</template>

<script>
    import AirlineService from "@/services/airline.service";
    import TicketService from "@/services/ticket.service";

    export default {
        name: "AirlinesPage",
        data() {
            return {
                // Airline
                airline: null,
                airlines: [{
                    text: 'Choose:',
                    value: null,
                }],
                table_airlines: [],
                airline_fields: [
                    {key: 'ID'},
                    {key: 'name'},
                    {key: 'link'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ],

                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',

                // Tickets
                fields: [
                    {key: 'airline'},
                    {key: 'airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'ticket_ID'},
                    {key: 'departure_place', label: 'From'},
                    {
                        key: 'departure_airport_name',
                        label: 'Departure Airport Name',
                        thClass: 'd-none',
                        tdClass: 'd-none'
                    },
                    {key: 'departure_airport_ID', label: 'Departure Airport ID'},
                    {key: 'arrival_place', label: 'To'},
                    {key: 'arrival_airport_name', label: 'Arrival Airport Name', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'arrival_airport_ID', label: 'Arrival Airport ID'},
                    {key: 'departure_date', label: 'Date'},
                    {key: 'flight_ID', label: 'Flight ID'},
                    {key: 'count', label: 'Remaining count'}
                ],
                tickets: [],

                infoModal: {
                    id: 'delete-modal',
                    content: ''
                }
            }
        },
        methods: {
            fetchAirlines() {
                this.airlines = [{
                    text: 'Choose:',
                    value: null,
                }]
                AirlineService.getAllAirlines().then(
                    response => {
                        response.data.forEach((airline) => {
                            this.airlines.push({
                                text: airline.name,
                                value: airline.id
                            })
                            this.table_airlines.push({
                                ID: airline.id,
                                name: airline.name,
                                link: airline.link,
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
            getTicketsForAirline() {
                this.showDismissibleAlert = false
                if (this.airline != null) {
                    TicketService.getTicketByAirline(this.airline).then(
                        response => {
                            this.tickets = []
                            response.data.forEach((ticket) => {
                                this.tickets.push({
                                    airline: ticket.airline,
                                    airline_link: ticket.airline_link,
                                    ticket_ID: ticket.ticket_ID,
                                    departure_place: ticket.departure_place,
                                    departure_airport_name: ticket.departure_airport_name,
                                    departure_airport_ID: ticket.departure_airport_ID,
                                    arrival_place: ticket.arrival_place,
                                    arrival_airport_name: ticket.arrival_airport_name,
                                    arrival_airport_ID: ticket.arrival_airport_ID,
                                    return_flight_id: ticket.return_flight_id,
                                    departure_date: ticket.departure_date,
                                    flight_ID: ticket.flight_ID,
                                    count: ticket.count
                                })
                                this.showDismissibleAlert = false
                            })
                        },
                        error => {
                            this.tickets = []
                            if (error.response) this.message = error.response.data.message || error.message
                            else this.message = error.toString()
                            this.variant = "warning"
                            this.showDismissibleAlert = true
                            this.successful = false;
                        }
                    )
                } else {
                    this.tickets = []
                }
            },
            editAirline(data) {
                this.$router.push({name: 'editAirline', params: data})
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
            deleteAirline() {
                AirlineService.deleteAirline(this.infoModal.content.ID).then(
                    response => {
                        if (response.status == 200) {
                            this.airline = null
                            this.tickets = []
                            this.table_airlines = []
                            this.fetchAirlines()
                            this.hideModal()
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
            this.fetchAirlines()
        }
    }
</script>

<style scoped>

</style>