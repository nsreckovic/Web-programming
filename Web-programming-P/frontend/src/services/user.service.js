import axios from 'axios';
import authHeader from './auth-header';

const API_USERS_URL = 'http://localhost:8080/users/';

class UserService {
    getAllCustomers() {
        return axios.get(API_USERS_URL + 'allCustomers', { headers: authHeader() });
    }

    getAll() {
        return axios.get(API_USERS_URL + 'all', { headers: authHeader() });
    }

    postEditUser(data, current_user_id) {
        return axios.post(API_USERS_URL + 'update/' + current_user_id, data, { headers: authHeader() });
    }

    deleteUser(user_id) {
        return axios.delete(API_USERS_URL + 'delete/' + user_id, { headers: authHeader() });
    }
}

export default new UserService();