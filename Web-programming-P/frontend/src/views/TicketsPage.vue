<template>
    <b-card style="max-width: 85%;" class="ml-auto mr-auto mt-3">
        <b-row class="ml-auto mb-2">
            <b-card-title>
                <h1>Tickets</h1>
            </b-card-title>
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

            <template v-slot:cell(edit)="row">
                <b-button size="sm" @click="editTicket(row.item)">Edit</b-button>
            </template>

            <template v-slot:cell(delete)="row">
                <b-button size="sm" variant="danger" @click="deletePrompt(row.item, row.index, $event.target)">
                    Delete
                </b-button>
            </template>
        </b-table>

        <b-modal :id="infoModal.id" hide-footer hide-header centered size="xl" :title="infoModal.title"
                 @hide="resetInfoModal">
            <h4 align="center">Are you sure you want to delete this ticket?</h4>
            <div class="text-center mt-5">
                <b-button class="mr-1" variant="outline-secondary" @click="hideModal">Cancel</b-button>
                <b-button class="mr-1" variant="danger" @click="deleteTicket">Delete</b-button>
            </div>
        </b-modal>
    </b-card>
</template>

<script>
    import TicketService from "@/services/ticket.service";

    export default {
        name: "TicketsPage",
        data() {
            return {
                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',

                // Tickets
                fields: [
                    {key: 'ticket_ID'},
                    {key: 'departure_place', label: 'From'},
                    {key: 'departure_airport_name', label: 'Departure Airport Name', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'departure_airport_ID', label: 'Departure Airport ID'},
                    {key: 'arrival_place', label: 'To'},
                    {key: 'arrival_airport_name', label: 'Arrival Airport Name', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'arrival_airport_ID', label: 'Arrival Airport ID'},
                    {key: 'departure_date', label: 'Date'},
                    {key: 'flight_ID', label: 'Flight ID'},
                    {key: 'flight_instance_ID', label: 'Flight Instance ID', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'count', label: 'Remaining count'},
                    {key: 'airline'},
                    {key: 'airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ],
                tickets: [],

                infoModal: {
                    id: 'delete-modal',
                    content: ''
                }
            }
        },
        methods: {
            fetchTickets() {
                this.showDismissibleAlert = false
                TicketService.getAllTickets().then(
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
                                departure_date: ticket.departure_date,
                                flight_ID: ticket.flight_ID,
                                flight_instance_ID: ticket.flight_instance_ID,
                                count: ticket.count,
                                Edit: null,
                                Delete: null,
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
            },
            editTicket(data) {
                this.$router.push({name: 'editTicket', params: data})
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
            deleteTicket() {
                TicketService.deleteTicket(this.infoModal.content.ticket_ID).then(
                    response => {
                        if (response.status == 200) {
                            this.tickets = []
                            this.fetchTickets()
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
            this.fetchTickets()
        }
    }
</script>

<style scoped>

</style>