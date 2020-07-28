import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/Home.vue';
import Login from './views/Login.vue';
import NewAirline from "@/views/new/NewAirline";
import NewAirport from "@/views/new/NewAirport";
import NewUser from "@/views/new/NewUser";
import NewFlight from "@/views/new/NewFlight";
import NewFlightInstance from "@/views/new/NewFlightInstance";
import NewReservation from "@/views/new/NewReservation";
import NewTicket from "@/views/new/NewTicket";
import EditReservation from "@/views/edit/EditReservation";
import AirlinesPage from "@/views/AirlinesPage";
import EditAirline from "@/views/edit/EditAirline";
import TicketsPage from "@/views/TicketsPage";
import EditTicket from "@/views/edit/EditTicket";
import Profile from "@/views/Profile";
import FlightInstancesPage from "@/views/FlightInstancesPage";
import FlightsPage from "@/views/FlightsPage";
import AirportsPage from "@/views/AirportsPage";
import UsersPage from "@/views/UsersPage";
import EditAirport from "@/views/edit/EditAirport";
import EditFlight from "@/views/edit/EditFlight";
import EditFlightInstance from "@/views/edit/EditFlightInstance";
import EditUser from "@/views/edit/EditUser";

Vue.use(Router);

export const router = new Router({
    mode: 'history',
    routes: [
        // HOME
        {
            path: '/',
            name: 'home',
            component: Home
        },

        // ADD ROUTES
        {
            path: '/airlines/new',
            name: 'newAirline',
            component: NewAirline
        },
        {
            path: '/airports/new',
            name: 'newAirport',
            component: NewAirport
        },
        {
            path: '/flights/new',
            name: 'newFlight',
            component: NewFlight
        },
        {
            path: '/flightInstances/new',
            name: 'newFlightInstance',
            component: NewFlightInstance
        },
        {
            path: '/tickets/new',
            name: 'newTicket',
            component: NewTicket
        },
        {
            path: '/reservations/new',
            name: 'newReservation',
            component: NewReservation
        },
        {
            path: '/users/new',
            component: NewUser
        },

        // EDIT ROUTES
        {
            path: '/airlines/edit',
            name: 'editAirline',
            component: EditAirline
        },
        {
            path: '/airports/edit',
            name: 'editAirport',
            component: EditAirport
        },
        {
            path: '/flights/edit',
            name: 'editFlight',
            component: EditFlight
        },
        {
            path: '/flightInstances/edit',
            name: 'editFlightInstance',
            component: EditFlightInstance
        },
        {
            path: '/tickets/edit',
            name: 'editTicket',
            component: EditTicket
        },
        {
            path: '/reservations/edit',
            name: 'editReservation',
            component: EditReservation
        },
        {
            path: '/users/edit',
            name: 'editUser',
            component: EditUser
        },

        // OTHER ROUTES
        {
            path: '/home',
            component: Home
        },
        {
            path: '/login',
            component: Login
        },
        {
            path: '/profile',
            name: 'profile',
            component: Profile
        },
        {
            path: '/airlines',
            component: AirlinesPage
        },
        {
            path: '/tickets',
            component: TicketsPage
        },
        {
            path: '/flightInstances',
            component: FlightInstancesPage
        },
        {
            path: '/flights',
            component: FlightsPage
        },
        {
            path: '/airports',
            component: AirportsPage
        },
        {
            path: '/users',
            component: UsersPage
        },
    ]
});