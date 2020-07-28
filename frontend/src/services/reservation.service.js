import axios from 'axios';
import authHeader from './auth-header';

const API_RESERVATIONS_URL = 'http://localhost:8080/reservations/';

class ReservationService {
    postAddReservation(data) {
        return axios.post(API_RESERVATIONS_URL + 'add', data, { headers: authHeader() });
    }

    postUpdateReservation(data, reservation_id) {
        return axios.post(API_RESERVATIONS_URL + 'update/' + reservation_id, data, { headers: authHeader() });
    }

    getAllReservations(data) {
        return axios.post(API_RESERVATIONS_URL + 'all', data,{ headers: authHeader() });
    }

    async getReservationByID(data) {
        return await axios.get(API_RESERVATIONS_URL + data, { headers: authHeader() });
    }

    deleteReservation(reservation) {
        return axios.delete(API_RESERVATIONS_URL + 'delete/' + reservation, { headers: authHeader() });
    }
}

export default new ReservationService();