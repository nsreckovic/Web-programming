import axios from 'axios';
import authHeader from './auth-header';

const API_FLIGHTS_URL = 'http://localhost:8080/flights/';

class FlightService {
    postAddFlight(data) {
        return axios.post(API_FLIGHTS_URL + 'add', data, { headers: authHeader() });
    }

    postEditFlight(data, current_flight_id) {
        return axios.post(API_FLIGHTS_URL + 'update/' + current_flight_id, data, { headers: authHeader() });
    }

    getAllFlights() {
        return axios.get(API_FLIGHTS_URL + 'all', { headers: authHeader() });
    }

    deleteFlight(flight_ID) {
        return axios.delete(API_FLIGHTS_URL + 'delete/' + flight_ID, { headers: authHeader() });
    }
}

export default new FlightService();