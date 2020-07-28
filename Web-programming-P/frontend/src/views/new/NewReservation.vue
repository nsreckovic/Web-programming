<template>
    <div>
        <b-card style="max-width: 55rem;" class="ml-auto mr-auto mt-3">
            <b-row>
                <b-col>
                    <b-card-title>New Reservation</b-card-title>
                </b-col>
                <b-col align="right">
                    <font-awesome-icon v-b-toggle.collapse-1 icon="filter"/>
                </b-col>
            </b-row>

            <b-form @submit.prevent="handleSubmitAddReservation">

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
                    <b-button type="submit" variant="primary" class="align-self-center">Submit</b-button>
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
        name: "NewReservation",
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
                reservation: new Reservation(null, null, null, null),
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false
            }
        },
        methods: {
            departureDateClicked() {
                this.min_return = this.departure_date_filter
            },
            returnDateClicked() {
                this.max_departure = this.return_date_filter
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
            filter() {
                this.departureTickets()
                this.fetchPossibleReturnTickets()
            },
            filterReset() {
                this.from_date_filter = null
                this.to_date_filter = null
                this.from_filter = null
                this.to_filter = null
                this.airline = null
                this.departure_tickets = [{
                    text: 'Select a departure ticket',
                    value: null,
                }]
                this.departureTickets()
            },
            fetchPossibleReturnTickets() {
                this.showDismissibleAlert = false
                this.reservation.return_ticket_ID = null
                this.return_tickets = [{
                    text: 'Select a departure ticket',
                    value: null,
                }]
                if (!this.reservation.departure_ticket_ID) return

                TicketService.getReturnFilteredTickets({
                    current_departure_ticket_id: this.reservation.departure_ticket_ID,
                    to_date_filter: this.to_date_filter,
                    airline: this.airline
                }).then(
                    response => {
                        response.data.forEach((ticket) => {
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
            handleSubmitAddReservation() {
                this.message = ''
                var dateFormat = require('dateformat');
                var moment = new Date();
                this.reservation.date = dateFormat(moment, "yyyy-mm-dd HH:MM:ss")
                ReservationService.postAddReservation(this.reservation).then(
                    response => {
                        this.message = response.data.message;
                        this.successful = true;
                        this.$router.push('/');
                    },
                    error => {
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "error"
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
            if (!this.currentUser()) {
                this.$router.push('/');
            }
            this.airportFilter()
            this.fetchAirlines()
            this.departureTickets()
            if (!this.isAdminLoggedIn) this.reservation.user_ID = this.$store.state.auth.user.id
            else {
                UserService.getAllCustomers().then(
                    response => {
                        response.data.forEach((user) => {
                            this.users.push({
                                text: user.username + ' (ID: ' + user.id + ')',
                                value: user.id
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
            }


        }
    }
</script>

<style scoped>

</style>