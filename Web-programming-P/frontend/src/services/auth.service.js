import axios from 'axios';
import authHeader from './auth-header';

const API_USERS_URL = 'http://localhost:8080/users/';

class AuthService {
    login(user) {
        return axios
            .post(API_USERS_URL + 'login', {
                username: user.username,
                password: user.password1
            })
            .then(response => {
                if (response.data.jwt) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }
                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(user) {
        return axios.post(API_USERS_URL + 'register', {
            username: user.username,
            password1: user.password1,
            password2: user.password2,
            type: user.type,
        },{ headers: authHeader() });
    }
}

export default new AuthService();