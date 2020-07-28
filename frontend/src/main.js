import Vue from 'vue'
import App from './App.vue'
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import Notifications from 'vue-notification'
import {router} from "./router"
import axios from 'axios'
import store from './store'
import {library} from '@fortawesome/fontawesome-svg-core';
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome';
import {
    faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt, faPlane, faPlus, faTicketAlt, faPlaneDeparture,
    faPlaneArrival, faBuilding, faIndustry, faGlobeAfrica, faCalendarCheck, faLocationArrow, faFilter, faTable
} from '@fortawesome/free-solid-svg-icons';

library.add(faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt, faPlane, faPlus, faTicketAlt, faPlaneDeparture,
    faPlaneArrival, faBuilding, faIndustry, faGlobeAfrica, faCalendarCheck, faLocationArrow, faFilter, faTable);

// BootstrapVue
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(Notifications);
Vue.component('font-awesome-icon', FontAwesomeIcon);

Vue.config.productionTip = false

axios.defaults.headers.post['Content-Type'] = 'application/json';
window.axios = axios;

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app')
