import axios from 'axios';
import authHeader from './auth-header';

const API_FLIGHT_INSTANCES_URL = 'http://localhost:8080/flightInstances/';

class FlightInstanceService {
    postAddFlightInstance(data) {
        return axios.post(API_FLIGHT_INSTANCES_URL + 'add', data, { headers: authHeader() });
    }

    postUpdateFlightInstance(flightInstance_id, data) {
        return axios.post(API_FLIGHT_INSTANCES_URL + 'update/' + flightInstance_id, data, { headers: authHeader() });
    }

    getAllFlightInstances() {
        return axios.get(API_FLIGHT_INSTANCES_URL + 'all', { headers: authHeader() });
    }

    deleteFlightInstance(flight_instance_id) {
        return axios.delete(API_FLIGHT_INSTANCES_URL + 'delete/' + flight_instance_id, { headers: authHeader() });
    }
}

export default new FlightInstanceService();