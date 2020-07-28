import axios from 'axios';
import authHeader from './auth-header';

const API_AIRPORTS_URL = 'http://localhost:8080/airports/';

class AirportService {
    postAddAirport(data) {
        return axios.post(API_AIRPORTS_URL + 'add', data, { headers: authHeader() });
    }

    postUpdateAirport(data, current_airport_id) {
        return axios.post(API_AIRPORTS_URL + 'update/' + current_airport_id, data, { headers: authHeader() });
    }

    getAllAirports() {
        return axios.get(API_AIRPORTS_URL + 'all', { headers: authHeader() });
    }

    deleteAirport(airport_id) {
        return axios.delete(API_AIRPORTS_URL + 'delete/' + airport_id, { headers: authHeader() });
    }
}

export default new AirportService();