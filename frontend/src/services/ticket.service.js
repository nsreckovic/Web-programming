import axios from 'axios';
import authHeader from './auth-header';

const API_TICKETS_URL = 'http://localhost:8080/tickets/';

class TicketService {
    postAddTicket(data) {
        return axios.post(API_TICKETS_URL + 'add', data, { headers: authHeader() });
    }

    postEditTicket(data, ticket_ID) {
        return axios.post(API_TICKETS_URL + 'update/' + ticket_ID, data, { headers: authHeader() });
    }

    getAllTickets() {
        return axios.get(API_TICKETS_URL + 'all', { headers: authHeader() });
    }

    getTicketByAirline(airline) {
        return axios.get(API_TICKETS_URL + 'airline/' + airline, { headers: authHeader() });
    }

    getTicketByID(ticket_ID) {
        return axios.get(API_TICKETS_URL + ticket_ID, { headers: authHeader() });
    }

    async getDepartureFilteredTickets(data) {
        return await axios.post(API_TICKETS_URL + 'departureFiltered', data,{ headers: authHeader() });
    }

    async getReturnFilteredTickets(data) {
        return await axios.post(API_TICKETS_URL + 'returnFiltered', data,{ headers: authHeader() });
    }

    getTicketsForUser(username) {
        return axios.get(API_TICKETS_URL + 'for_user/' + username, { headers: authHeader() });
    }

    deleteTicket(ID) {
        return axios.delete(API_TICKETS_URL + 'delete/' + ID, { headers: authHeader() })
    }
}

export default new TicketService();