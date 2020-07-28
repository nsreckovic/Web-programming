<template>
    <div>
        <b-card style="max-width: 95%;" class="ml-auto mr-auto mt-3">
            <b-row class="ml-auto mb-2">
                <b-card-title>
                    <h2 v-if="isAdminLoggedIn">All Reservations</h2>
                    <h2 v-else>My Reservations</h2>
                </b-card-title>
                <b-col align="right">
                    <font-awesome-icon v-b-toggle.filters icon="filter"/>
                </b-col>
            </b-row>

            <b-collapse id="filters" class="mb-4">
                <b-card title="Filters" class="mb-2">
                    <b-row>
                        <b-col>
                            <b-form-group id="reservations-filter-group-type" label="Type:"
                                          label-for="reservations-filter-type">
                                <b-form-select id="reservations-filter-type" v-model="type_filter"
                                               :plain="true" :options="types" class="form-control">
                                </b-form-select>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group id="reservations-filter-group-from" label="From:"
                                          label-for="reservations-filter-from">
                                <b-form-select id="reservations-filter-from" v-model="from_filter"
                                               :plain="true" :options="from_airports" class="form-control">
                                </b-form-select>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group id="reservations-filter-group-to" label="To:"
                                          label-for="reservations-filter-to">
                                <b-form-select id="reservations-filter-to" v-model="to_filter"
                                               :plain="true" :options="to_airports" class="form-control">
                                </b-form-select>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group id="reservation-filter-group-departure-date" label="Departure date:"
                                          label-for="reservation-filter-departure-date">
                                <b-form-datepicker id="reservation-filter-departure-date"
                                                   v-model="from_date_filter"
                                                   :min="min_departure" :max="max_departure"
                                                   class="form-control"
                                                   @input="departureDateClicked"></b-form-datepicker>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group id="reservation-filter-group-return-date" label="Return date:"
                                          label-for="reservation-filter-return-date">
                                <b-form-datepicker id="reservation-filter-return-date"
                                                   v-model="to_date_filter"
                                                   :min="min_return" :max="max_return"
                                                   class="form-control"
                                                   @input="returnDateClicked"></b-form-datepicker>
                            </b-form-group>
                        </b-col>
                        <b-col>
                            <b-form-group id="new-res-filter-group-airline" label="Airline:"
                                          label-for="new-res-filter-airline">
                                <b-form-select id="new-res-filter-airline" v-model="airline" :plain="true"
                                               :options="airlines" class="form-control">
                                </b-form-select>
                            </b-form-group>
                        </b-col>
                    </b-row>
                    <b-row v-if="isAdminLoggedIn" class="mb-3">
                        <b-col></b-col>
                        <b-col></b-col>
                        <b-col></b-col>
                        <b-col></b-col>
                        <b-col></b-col>
                        <b-col>
                            <b-form-select id="reservations-user" v-model="username_filter"
                                           :plain="true" :options="users" class="form-control" required>
                            </b-form-select>
                        </b-col>
                    </b-row>
                    <div class="text-right">
                        <b-button @click="getAllReservations" class="mr-1" variant="primary">Filter</b-button>
                        <b-button @click="filterReset" variant="outline-secondary">Reset</b-button>
                    </div>
                </b-card>
            </b-collapse>

            <b-alert v-model="showDismissibleAlert" :variant="variant" dismissible class="mb-4">
                {{message}}
            </b-alert>

            <b-table show-empty
                     small
                     stacked="md"
                     :items="reservations"
                     :fields="fields"
                     striped
                     hover
                     class="text-center mb-5">

                <template v-slot:cell(airline)="row">
                    <a :href=row.item.airline_link target="_blank" class="text-dark">{{row.item.airline}}</a>
                </template>

                <template v-slot:cell(return_airline)="row">
                    <a :href=row.item.return_airline_link target="_blank"
                       class="text-dark">{{row.item.return_airline}}</a>
                </template>

                <template v-slot:cell(edit)="row">
                    <b-button size="sm" @click="editReservation(row.item)">Edit</b-button>
                </template>

                <template v-slot:cell(delete)="row">
                    <b-button size="sm" variant="danger" @click="info(row.item, row.index, $event.target)">
                        Delete
                    </b-button>
                </template>
            </b-table>

            <h2 v-if="!isAdminLoggedIn" class="mt-4"> My Tickets</h2>
            <b-table v-if="!isAdminLoggedIn"
                     show-empty
                     small
                     stacked="md"
                     :items="tickets"
                     :fields="ticket_fields"
                     striped
                     hover
                     class="text-center">

                <template v-slot:cell(airline)="row">
                    <a :href=row.item.airline_link target="_blank" class="text-dark">{{row.item.airline}}</a>
                </template>

            </b-table>

            <b-modal :id="infoModal.id" hide-footer hide-header centered size="lg" :title="infoModal.title"
                     @hide="resetInfoModal">
                <h4 align="center">Are you sure you want to delete this reservation?</h4>
                <div class="text-center mt-5">
                    <b-button class="mr-1" variant="outline-secondary" @click="hideModal">Cancel</b-button>
                    <b-button class="mr-1" variant="danger" @click="deleteReservation">Delete</b-button>
                </div>
            </b-modal>
        </b-card>
    </div>
</template>

<script>
    import ReservationService from '@/services/reservation.service';
    import AirlineService from "@/services/airline.service";
    import AirportService from "@/services/airport.service";
    import UserService from "@/services/user.service";
    import TicketService from "@/services/ticket.service";

    export default {
        name: "Reservations",
        data() {
            const now = new Date()
            const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
            const minDate = new Date(today)
            const maxDate = new Date(today)
            maxDate.setMonth(maxDate.getMonth() + 6)
            return {
                // FILTERS
                // Type
                username_filter: null,
                users: [{
                    text: 'User:',
                    value: null,
                }],
                type_filter: null,
                types: ['All', 'One-way', 'Round-trip'],
                // Airline
                airline: null,
                airlines: [{
                    text: 'Airline:',
                    value: null,
                }],
                // Dates
                min_departure: minDate,
                max_departure: maxDate,
                min_return: minDate,
                max_return: maxDate,
                from_date_filter: null,
                to_date_filter: null,
                // From / To
                from_filter: null,
                from_airports: [{
                    text: 'From:',
                    value: null,
                }],
                to_filter: null,
                to_airports: [{
                    text: 'To:',
                    value: null,
                }],

                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',

                // Reservations
                fields: [
                    {key: 'id', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'reservation_date', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'from'},
                    {key: 'to'},
                    {key: 'from_ticket_id', label: 'Ticket To'},
                    {key: 'flight_id', label: 'Flight To'},
                    {key: 'departure_date'},
                    {key: 'airline'},
                    {key: 'airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'return_ticket_id', label: 'Return Ticket'},
                    {key: 'return_flight_id', label: 'Return Flight'},
                    {key: 'return_date'},
                    {key: 'return_airline'},
                    {key: 'return_airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'user_id', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'username'},
                    {key: 'expired'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ],
                reservations: [],

                // Tickets
                ticket_fields: [
                    {key: 'ticket_ID'},
                    {key: 'departure_place', label: 'From'},
                    {key: 'arrival_place', label: 'To'},
                    {key: 'departure_airport_ID', label: 'Departure Airport ID'},
                    {
                        key: 'departure_airport_name',
                        label: 'Departure Airport Name',
                        thClass: 'd-none',
                        tdClass: 'd-none'
                    },
                    {key: 'arrival_airport_ID', label: 'Arrival Airport ID'},
                    {key: 'arrival_airport_name', label: 'Arrival Airport Name', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'departure_date', label: 'Date'},
                    {key: 'flight_ID', label: 'Flight ID'},
                    {key: 'airline'},
                    {key: 'airline_link', thClass: 'd-none', tdClass: 'd-none'},
                ],
                tickets: [],


                infoModal: {
                    id: 'info-modal',
                    title: '',
                    content: ''
                }
            }
        },
        methods: {
            fetchTickets() {
                this.showDismissibleAlert = false
                TicketService.getTicketsForUser(this.$store.state.auth.user.username).then(
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
            departureDateClicked() {
                this.min_return = this.from_date_filter
            },
            returnDateClicked() {
                this.max_departure = this.to_date_filter
            },
            fetchAirlines() {
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
            },
            fetchAirports() {
                AirportService.getAllAirports().then(
                    response => {
                        response.data.forEach((airport) => {
                            this.from_airports.push({
                                text: airport.id + ' - ' + airport.place + ' (' + airport.name + ')',
                                value: airport.id
                            })
                            this.to_airports.push({
                                text: airport.id + ' - ' + airport.place + ' (' + airport.name + ')',
                                value: airport.id
                            })

                        })
                    },
                    error => {
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "warning"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            fetchUsers() {
                UserService.getAllCustomers().then(
                    response => {
                        response.data.forEach((user) => {
                            this.users.push({
                                text: user.username,
                                value: user.username
                            })
                        })
                    },
                    error => {
                        this.message = error.response.data.message
                        this.showDismissibleAlert = true
                        this.successful = false;
                        this.variant = "error"
                    }
                )
            },
            filterReset() {
                this.from_date_filter = null
                this.to_date_filter = null
                this.from_filter = null
                this.to_filter = null
                this.airline = null
                this.type_filter = null
                if (this.isAdminLoggedIn) this.username_filter = null
                this.min_return = null
                this.max_departure = null
                this.getAllReservations()
            },
            editReservation(data) {
                var moment = new Date();
                moment.setDate(moment.getDate() + 1)
                var departure_date = Date.parse(data.departure_date)

                if (this.isAdminLoggedIn) this.$router.push({name: 'editReservation', params: data})
                else {
                    console.log("")
                    if (moment.getTime() <= departure_date) {
                        this.$router.push({name: 'editReservation', params: data})
                    } else {
                        this.message = 'Error: The reservation can only be edited up to 24 hours before departure!'
                        this.variant = "danger"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                }

            },
            info(item, index, button) {
                this.infoModal.title = `Row index: ${item.id}`
                this.infoModal.content = item
                this.$root.$emit('bv::show::modal', this.infoModal.id, button)
            },
            resetInfoModal() {
                this.infoModal.title = ''
                this.infoModal.content = ''
            },
            hideModal() {
                this.$root.$emit('bv::hide::modal', this.infoModal.id)
            },
            getAllReservations() {
                ReservationService.getAllReservations({
                    from_date_filter: this.from_date_filter,
                    to_date_filter: this.to_date_filter,
                    from_filter: this.from_filter,
                    to_filter: this.to_filter,
                    airline: this.airline,
                    type: this.type_filter,
                    username: this.username_filter
                }).then(
                    response => {
                        this.reservations = []
                        response.data.forEach((reservation) => {
                            var moment = new Date();
                            // moment.setDate(moment.getDate() + 2)
                            var row_variant = ''
                            var expired = 'No'

                            if (reservation.return_date != null) {
                                var return_date = Date.parse(reservation.return_date)
                                if (moment >= return_date) {
                                    row_variant = 'secondary'
                                    expired = 'Yes'
                                }
                            } else {
                                var departure_date = Date.parse(reservation.departure_date)
                                if (moment >= departure_date) {
                                    row_variant = 'secondary'
                                    expired = 'Yes'
                                }
                            }

                            this.reservations.push({
                                id: reservation.id,
                                reservation_date: reservation.reservation_date,
                                from: reservation.from,
                                to: reservation.to,
                                flight_id: reservation.flight_id,
                                departure_date: reservation.departure_date,
                                from_ticket_id: reservation.from_ticket_id,
                                airline: reservation.airline,
                                airline_link: reservation.airline_link,
                                return_flight_id: reservation.return_flight_id,
                                return_date: reservation.return_date,
                                return_ticket_id: reservation.return_ticket_id,
                                return_airline: reservation.return_airline,
                                return_airline_link: reservation.return_airline_link,
                                user_id: reservation.user_id,
                                username: reservation.username,
                                expired: expired,
                                Edit: null,
                                Delete: null,
                                _rowVariant: row_variant
                            })
                            this.showDismissibleAlert = false
                        })
                    },
                    error => {
                        this.reservations = []
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "warning"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            currentUser() {
                return this.$store.state.auth.user;
            },
            deleteReservation() {
                var moment = new Date();
                moment.setDate(moment.getDate() + 1)
                var departure_date = Date.parse(this.infoModal.content.departure_date)

                if (this.isAdminLoggedIn || moment.getTime() <= departure_date) {
                    ReservationService.deleteReservation(this.infoModal.content.id).then(
                        response => {
                            if (response.status == 200) {
                                this.hideModal()
                                this.getAllReservations()
                                this.fetchTickets()
                            }
                        },
                        error => {
                            if (error.response) this.message = error.response.data.message || error.message
                            else this.message = error.toString()
                            this.variant = "warning"
                            this.showDismissibleAlert = true
                            this.successful = false;
                        }
                    )
                } else {
                    this.message = 'Error: The reservation can only be canceled up to 24 hours before departure!'
                    this.variant = "danger"
                    this.showDismissibleAlert = true
                    this.successful = false;
                    this.hideModal()
                }


            }
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
            if (!this.currentUser()) {
                this.$router.push('/login');
            }
            if (!this.isAdminLoggedIn) {
                this.username_filter = this.$store.state.auth.user.username
                this.fields = [
                    {key: 'id', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'reservation_date', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'from'},
                    {key: 'to'},
                    {key: 'from_ticket_id', label: 'Ticket To'},
                    {key: 'flight_id', label: 'Flight To'},
                    {key: 'departure_date'},
                    {key: 'airline'},
                    {key: 'airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'return_ticket_id', label: 'Return Ticket'},
                    {key: 'return_flight_id', label: 'Return Flight'},
                    {key: 'return_date'},
                    {key: 'return_airline'},
                    {key: 'return_airline_link', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'user_id', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'username', thClass: 'd-none', tdClass: 'd-none'},
                    {key: 'expired'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ]
                this.fetchTickets()
            } else {
                this.fetchUsers()
            }
            this.fetchAirports()
            this.fetchAirlines()
            this.getAllReservations()
        }
    }

</script>

<style scoped>

</style>