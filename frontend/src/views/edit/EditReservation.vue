<template>
    <div>
        <b-card style="max-width: 55rem;" class="ml-auto mr-auto mt-3">
            <b-row>
                <b-col>
                    <b-card-title>Edit Reservation</b-card-title>
                </b-col>
                <b-col align="right">
                    <font-awesome-icon v-b-toggle.collapse-1 icon="filter"/>
                </b-col>
            </b-row>

            <b-form @submit.prevent="handleSubmitEditReservation">

                <b-collapse id="collapse-1" class="mt-2">
                    <b-card title="Filters" class="mb-2">
                        <b-row>
                            <b-col>
                                <b-form-group id="new-res-filter-group-from" label="From:"
                                              label-for="new-res-filter-from">
                                    <b-form-select id="new-res-filter-from" v-model="from_filter"
                                                   :plain="true" :options="from_airports" class="form-control">
                                    </b-form-select>
                                </b-form-group>
                            </b-col>
                            <b-col>
                                <b-form-group id="new-res-filter-group-to" label="To:" label-for="new-res-filter-to">
                                    <b-form-select id="new-res-filter-to" v-model="to_filter"
                                                   :plain="true" :options="to_airports" class="form-control">
                                    </b-form-select>
                                </b-form-group>
                            </b-col>
                        </b-row>

                        <b-row>
                            <b-col>
                                <b-form-group id="new-res-filter-group-departure-date" label="Departure date:"
                                              label-for="new-res-filter-departure-date">
                                    <b-form-datepicker id="new-res-filter-departure-date"
                                                       v-model="from_date_filter"
                                                       :min="min_departure" :max="max_departure"
                                                       @input="departureDateClicked"></b-form-datepicker>
                                </b-form-group>
                            </b-col>
                            <b-col>
                                <b-form-group id="new-res-filter-group-return-date" label="Return date:"
                                              label-for="new-res-filter-return-date">
                                    <b-form-datepicker id="new-res-filter-return-date"
                                                       v-model="to_date_filter"
                                                       :min="min_return" :max="max_return"
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
                        <div class="text-right">
                            <b-button @click="filter" class="mr-1" variant="primary">Filter</b-button>
                            <b-button @click="filterReset" variant="outline-secondary">Reset</b-button>
                        </div>
                    </b-card>
                </b-collapse>

                <b-form-group id="input-group-reservation-from" label="From ticket:" label-for="input-reservation-from">
                    <b-form-select id="input-reservation-from" v-model="reservation.departure_ticket_ID"
                                   @change="fetchPossibleReturnTickets"
                                   :plain="true" :options="departure_tickets" class="form-control" required>
                    </b-form-select>
                </b-form-group>

                <b-form-group id="input-group-reservation-to" label="Return ticket:" label-for="input-reservation-to">
                    <b-form-select id="input-reservation-to" v-model="reservation.return_ticket_ID"
                                   :plain="true" :options="return_tickets" class="form-control">
                    </b-form-select>
                </b-form-group>

                <b-form-group v-if="isAdminLoggedIn" id="input-group-reservation-user" label="Reservation for user:"
                              label-for="input-reservation-user">
                    <b-form-select id="input-reservation-user" v-model="reservation.user_ID"
                                   :plain="true" :options="users" class="form-control" required>
                    </b-form-select>
                </b-form-group>

                <div class="text-center">
                    <b-button class="align-self-center mr-1" variant="outline-secondary" @click.prevent="cancel">
                        Cancel
                    </b-button>
                    <b-button type="submit" variant="danger" class="align-self-center">Submit</b-button>
                </div>

            </b-form>

            <br v-if="showDismissibleAlert">

            <b-alert v-model="showDismissibleAlert" :variant="variant" dismissible>
                {{message}}
            </b-alert>

        </b-card>
    </div>
</template>

<script>
    import Reservation from "@/models/reservation";
    import ReservationService from '@/services/reservation.service';
    import TicketService from "@/services/ticket.service";
    import UserService from "@/services/user.service";
    import AirportService from "@/services/airport.service";
    import AirlineService from '../../services/airline.service';

    export default {
        name: "EditReservation",
        data() {
            const now = new Date()
            const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
            const minDate = new Date(today)
            const maxDate = new Date(today)
            maxDate.setMonth(maxDate.getMonth() + 6)
            return {
                airline: null,
                airlines: [{
                    text: 'Airline:',
                    value: null,
                }],

                min_departure: minDate,
                max_departure: maxDate,

                min_return: minDate,
                max_return: maxDate,

                from_date_filter: null,
                to_date_filter: null,

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

                departure_tickets: [{
                    text: 'Select a departure ticket',
                    value: null,
                }],
                return_tickets: [{
                    text: 'Select a return ticket',
                    value: null,
                }],
                users: [{
                    text: 'Select an user',
                    value: null,
                }],


                current_departure_ticket: {
                    airline: null,
                    arrival_airport_ID: null,
                    arrival_airport_name: null,
                    arrival_place: null,
                    count: null,
                    departure_airport_ID: null,
                    departure_airport_name: null,
                    departure_date: null,
                    departure_place: null,
                    flight_ID: null,
                    flight_instance_ID: null,
                    ticket_ID: null,
                },
                current_return_ticket: {
                    airline: null,
                    arrival_airport_ID: null,
                    arrival_airport_name: null,
                    arrival_place: null,
                    count: null,
                    departure_airport_ID: null,
                    departure_airport_name: null,
                    departure_date: null,
                    departure_place: null,
                    flight_ID: null,
                    flight_instance_ID: null,
                    ticket_ID: null,
                },


                reservation: new Reservation(null, null, null, null),
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false
            }
        },
        methods: {
            cancel() {
                this.$router.back()
            },
            departureDateClicked() {
                this.min_return = this.departure_date_filter
            },
            returnDateClicked() {
                this.max_departure = this.return_date_filter
            },

            async fetchTicketByID(ticket_ID) {
                let response = await TicketService.getTicketByID(ticket_ID)
                return response.data
            },
            fetchOtherAvailableDepartureTickets() {
                TicketService.getDepartureFilteredTickets({
                    from_date_filter: this.from_date_filter,
                    to_date_filter: this.to_date_filter,
                    from_filter: this.from_filter,
                    to_filter: this.to_filter,
                    airline: this.airline,
                    current_departure_ticket_id: this.current_departure_ticket.ticket_ID
                }).then(
                    response => {
                        response.data.forEach((ticket) => {
                            this.departure_tickets.push({
                                text: ticket.flight_ID + ' : ' + ticket.departure_place + ' (' + ticket.departure_airport_ID + ') -> ' + ticket.arrival_place + ' (' + ticket.arrival_airport_ID + ') on ' + ticket.departure_date + ' by ' + ticket.airline,
                                value: ticket.ticket_ID
                            })
                            this.showDismissibleAlert = false
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
            fetchOtherAvailableReturnTickets() {
                TicketService.getReturnFilteredTickets({
                    from_date_filter: this.from_date_filter,
                    to_date_filter: this.to_date_filter,
                    from_filter: this.from_filter,
                    to_filter: this.to_filter,
                    airline: this.airline,
                    current_departure_ticket_id: this.current_departure_ticket.ticket_ID,
                    current_return_ticket_id: this.current_return_ticket.ticket_ID
                }).then(
                    response => {
                        response.data.forEach((ticket) => {
                            this.return_tickets.push({
                                text: ticket.flight_ID + ' : ' + ticket.departure_place + ' (' + ticket.departure_airport_ID + ') -> ' + ticket.arrival_place + ' (' + ticket.arrival_airport_ID + ') on ' + ticket.departure_date + ' by ' + ticket.airline,
                                value: ticket.ticket_ID
                            })
                            this.showDismissibleAlert = false
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
            async fetchCurrentTickets(reservation_ID) {
                let response = await ReservationService.getReservationByID(reservation_ID).catch(error => {
                    this.message = error.response.data.message || error.message
                    this.variant = "danger"
                    this.showDismissibleAlert = true
                    this.successful = false;
                })
                if (response.status == 200) {
                    let reservation = response.data
                    this.current_departure_ticket = await this.fetchTicketByID(reservation.from_ticket_id)
                    if (reservation.return_ticket_id != null) this.current_return_ticket = await this.fetchTicketByID(reservation.return_ticket_id)

                    this.departure_tickets = [{
                        text: 'Select a departure ticket',
                        value: null,
                    }]
                    this.return_tickets = [{
                        text: 'Select a return ticket',
                        value: null,
                    }]

                    if (this.current_departure_ticket.ticket_ID != null) {
                        this.departure_tickets.push({
                            text: this.current_departure_ticket.flight_ID + ' : ' + this.current_departure_ticket.departure_place + ' (' + this.current_departure_ticket.departure_airport_ID + ') -> ' + this.current_departure_ticket.arrival_place + ' (' + this.current_departure_ticket.arrival_airport_ID + ') on ' + this.current_departure_ticket.departure_date + ' by ' + this.current_departure_ticket.airline,
                            value: this.current_departure_ticket.ticket_ID
                        })
                        this.fetchOtherAvailableDepartureTickets()
                        this.reservation.departure_ticket_ID = this.current_departure_ticket.ticket_ID
                    }

                    if (this.current_return_ticket.ticket_ID != null) {
                        this.return_tickets.push({
                            text: this.current_return_ticket.flight_ID + ' : ' + this.current_return_ticket.departure_place + ' (' + this.current_return_ticket.departure_airport_ID + ') -> ' + this.current_return_ticket.arrival_place + ' (' + this.current_return_ticket.arrival_airport_ID + ') on ' + this.current_return_ticket.departure_date + ' by ' + this.current_return_ticket.airline,
                            value: this.current_return_ticket.ticket_ID
                        })
                        this.fetchOtherAvailableReturnTickets()
                        this.reservation.return_ticket_ID = this.current_return_ticket.ticket_ID
                    } else {
                        this.current_return_ticket.ticket_ID = null
                        this.fetchOtherAvailableReturnTickets()
                    }
                }
            },

            filter() {
                this.reservation.departure_ticket_ID = null
                this.reservation.return_ticket_ID = null
                this.current_departure_ticket = null
                this.current_return_ticket = null

                this.departure_tickets = [{
                    text: 'Select a departure ticket',
                    value: null,
                }]
                this.return_tickets = [{
                    text: 'Select a return ticket',
                    value: null,
                }]

                this.departureTickets()
            },
            filterReset() {
                this.from_date_filter = null
                this.to_date_filter = null
                this.from_filter = null
                this.to_filter = null
                this.airline = null
                this.fetchCurrentTickets(this.$route.params.id)
                this.reservation.user_ID = this.$route.params.user_id
            },

            departureTickets() {
                TicketService.getDepartureFilteredTickets({
                    from_date_filter: this.from_date_filter,
                    to_date_filter: this.to_date_filter,
                    from_filter: this.from_filter,
                    to_filter: this.to_filter,
                    airline: this.airline
                }).then(
                    response => {
                        this.departure_tickets = [{
                            text: 'Select a departure ticket',
                            value: null,
                        }]
                        response.data.forEach((ticket) => {
                            if (ticket.count <= 0) {
                                if (this.reservation.departure_ticket_ID != ticket.ticket_ID) {
                                    return;
                                }
                            }
                            this.departure_tickets.push({
                                text: ticket.flight_ID + ' : ' + ticket.departure_place + ' (' + ticket.departure_airport_ID + ') -> ' + ticket.arrival_place + ' (' + ticket.arrival_airport_ID + ') on ' + ticket.departure_date + ' by ' + ticket.airline,
                                value: ticket.ticket_ID
                            })
                            this.showDismissibleAlert = false
                        })
                    },
                    error => {
                        this.departure_tickets = [{
                            text: 'Select a departure ticket',
                            value: null,
                        }]
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "warning"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            fetchPossibleReturnTickets() {
                this.showDismissibleAlert = false
                this.reservation.return_ticket_ID = null
                this.return_tickets = [{
                    text: 'Select a return ticket',
                    value: null,
                }]
                if (!this.reservation.departure_ticket_ID) return

                TicketService.getReturnFilteredTickets({
                    departure_ticket_ID: this.reservation.departure_ticket_ID,
                    to_date_filter: this.to_date_filter,
                    airline: this.airline
                }).then(
                    response => {
                        response.data.forEach((ticket) => {
                            if (ticket.count <= 0) {
                                if (this.reservation.return_ticket_ID != ticket.ticket_ID) {
                                    return;
                                }
                            }
                            this.return_tickets.push({
                                text: ticket.flight_ID + ' : ' + ticket.departure_place + ' (' + ticket.departure_airport_ID + ') -> ' + ticket.arrival_place + ' (' + ticket.arrival_airport_ID + ') on ' + ticket.departure_date + ' by ' + ticket.airline,
                                value: ticket.ticket_ID
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
            airportFilter() {
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
            currentUser() {
                return this.$store.state.auth.user;
            },

            handleSubmitEditReservation() {
                if ((this.reservation.departure_ticket_ID == this.$route.params.from_ticket_id) &&
                    (this.reservation.return_ticket_ID == this.$route.params.return_ticket_id) &&
                    (this.reservation.user_ID == this.$route.params.user_id)) {
                    this.$router.back()
                } else {
                    this.message = ''
                    var dateFormat = require('dateformat');
                    var moment = new Date();
                    this.reservation.date = dateFormat(moment, "yyyy-mm-dd HH:MM:ss")
                    ReservationService.postUpdateReservation(this.reservation, this.$route.params.id).then(
                        response => {
                            this.message = response.data.message;
                            this.successful = true;
                            this.$router.push('/');
                        },
                        error => {
                            if (error.response) this.message = error.response.data.message || error.message;
                            else this.message = error.toString()
                            this.variant = "error"
                            this.showDismissibleAlert = true
                            this.successful = false;
                        }
                    )
                }



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
            if (!this.currentUser()) this.$router.push('/');
            this.airportFilter()
            this.fetchAirlines()
            this.fetchCurrentTickets(this.$route.params.id)
            if (!this.isAdminLoggedIn) this.reservation.user_ID = this.$store.state.auth.user.id
            else {
                UserService.getAllCustomers().then(
                    response => {
                        response.data.forEach((user) => {
                            this.users.push({
                                text: user.username + ' (ID: ' + user.id + ')',
                                value: user.id
                            })
                            this.reservation.user_ID = this.$route.params.user_id
                        })
                    },
                    error => {
                        this.message = error.response.data.message
                        this.showDismissibleAlert = true
                        this.successful = false;
                        this.variant = "error"
                    }
                )
            }


        }
    }
</script>

<style scoped>

</style>