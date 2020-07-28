import axios from 'axios';
import authHeader from './auth-header';

const API_AIRLINES_URL = 'http://localhost:8080/airlines/';

class AirlineService {
    postAddAirline(data) {
        return axios.post(API_AIRLINES_URL + 'add', data, { headers: authHeader() });
    }

    postEditAirline(data, airline_id) {
        return axios.post(API_AIRLINES_URL + 'update/' + airline_id, data, { headers: authHeader() });
    }

    getAllAirlines() {
        return axios.get(API_AIRLINES_URL + 'all', { headers: authHeader() });
    }

    getAirlineById(airline_id) {
        return axios.get(API_AIRLINES_URL + airline_id, { headers: authHeader() });
    }

    deleteAirline(airline_id) {
        return axios.delete(API_AIRLINES_URL + 'delete/' + airline_id, { headers: authHeader() });
    }

}

export default new AirlineService();