<template>
    <div id="app">
        <b-navbar toggleable="lg" type="dark" variant="dark">
            <b-navbar-brand class="text-light">
                <strong>WebAir</strong>
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item to="/home" class="nav-link pr-0">
                        <font-awesome-icon icon="home"/> Home
                    </b-nav-item>

                    <b-nav-item-dropdown v-if="isAdminLoggedIn" text="New" right class="nav-link px-0">
                        <template slot="button-content">
                            <font-awesome-icon icon="table"/> Pages
                        </template>
                        <b-dropdown-item to="/airlines"><font-awesome-icon icon="location-arrow"/> Airlines</b-dropdown-item>
                        <b-dropdown-item to="/airports"><font-awesome-icon icon="industry"/> Airports</b-dropdown-item>
                        <b-dropdown-item to="/flights"><font-awesome-icon icon="plane"/> Flights</b-dropdown-item>
                        <b-dropdown-item to="/flightInstances"><font-awesome-icon icon="plane-departure"/> Flight Instances</b-dropdown-item>
                        <b-dropdown-item to="/tickets"><font-awesome-icon icon="ticket-alt"/> Tickets</b-dropdown-item>
                        <b-dropdown-item to="/"><font-awesome-icon icon="calendar-check"/> Reservations</b-dropdown-item>
                        <b-dropdown-item to="/users"><font-awesome-icon icon="user"/> Users</b-dropdown-item>
                    </b-nav-item-dropdown>

                    <b-nav-item-dropdown v-if="isAdminLoggedIn" text="New" right class="nav-link pl-0">
                        <template slot="button-content">
                            <font-awesome-icon icon="plus"/> New
                        </template>
                        <b-dropdown-item to="/airlines/new"><font-awesome-icon icon="location-arrow"/> Airline</b-dropdown-item>
                        <b-dropdown-item to="/airports/new"><font-awesome-icon icon="industry"/> Airport</b-dropdown-item>
                        <b-dropdown-item to="/flights/new"><font-awesome-icon icon="plane"/> Flight</b-dropdown-item>
                        <b-dropdown-item to="/flightInstances/new"><font-awesome-icon icon="plane-departure"/> Flight Instance</b-dropdown-item>
                        <b-dropdown-item to="/tickets/new"><font-awesome-icon icon="ticket-alt"/> Ticket</b-dropdown-item>
                        <b-dropdown-item to="/reservations/new"><font-awesome-icon icon="calendar-check"/> Reservation</b-dropdown-item>
                        <b-dropdown-item to="/users/new"><font-awesome-icon icon="user-plus"/> User</b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item v-if="isCustomerLoggedIn" to="/reservations/new" class="nav-link pr-0">
                        <font-awesome-icon icon="calendar-check"/> New Reservation
                    </b-nav-item>
                </b-navbar-nav>

                <b-navbar-nav v-if="!currentUser" class="ml-auto">
                    <b-nav-item to="login">
                        <font-awesome-icon icon="sign-in-alt"/> Log In
                    </b-nav-item>
                </b-navbar-nav>

                <b-navbar-nav v-else class="ml-auto">
                    <b-nav-item to="/profile">
                        <font-awesome-icon icon="user"/> {{ currentUser.username }}
                    </b-nav-item>
                    <b-nav-item @click.prevent="logOut">
                        <font-awesome-icon icon="sign-out-alt"/> Log Out
                    </b-nav-item>

                </b-navbar-nav>
            </b-collapse>
        </b-navbar>

        <div>
            <router-view/>
        </div>
    </div>
</template>

<script>
    export default {
        computed: {
            currentUser() {
                return this.$store.state.auth.user;
            },
            isAdminLoggedIn() {
                if (this.currentUser && this.currentUser.type) {
                    if (this.currentUser.type === 'ADMIN') return true;
                }
                return false;
            },
            isCustomerLoggedIn() {
                if (this.currentUser && this.currentUser.type) {
                    if (this.currentUser.type === 'CUSTOMER') return true;
                }
                return false;
            }
        },
        methods: {
            logOut() {
                this.$store.dispatch('auth/logout');
                this.$router.push('/login');
            }
        }
    };
</script>

<style>

</style>
