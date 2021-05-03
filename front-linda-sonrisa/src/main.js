import Vue from 'vue'
import Vuex from 'vuex'
import App from './App.vue'
import VueRouter from "vue-router";
import store from './store'
import Datepicker from 'vuejs-datepicker';

import DatePicker from 'vue2-datepicker';
import 'vue2-datepicker/index.css';
Vue.use(DatePicker)
//import * as Cookies from "js-cookie";
import axios from 'axios'
Vue.prototype.$axios = axios


Vue.use(Datepicker)
Vue.use(Vuex)
Vue.use(VueRouter);
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)


Vue.config.productionTip = false
//import UserManagementComponent from "";
//home
import PresentationComponent from "./components/home/presentationComponent.vue";
Vue.component("presentation-component", PresentationComponent );
//Footer
import FooterComponent from "./views/Footer.vue";
Vue.component("footer-component", FooterComponent );

import LoginComponent              from "./components/home/loginComponent.vue";
import CreateUserComponent         from "./components/home/createUserComponent.vue";
import AppointmentRequestComponent from "./components/client/appointmentRequestComponent.vue";
import appointmentRequestComponentAct from "./components/client/appointmentRequestComponentAct.vue";
import EmployeeComponent           from "./components/employee/employeeComponent.vue"

const routes = [
  {
    name: "init",
    path: "/",
    component: PresentationComponent,
    meta: {
      title: "Linda sonrisa",
      auth: false
    }
  },
  {
    name: "login",
    path: "/acceder",
    component: LoginComponent,
    meta: {
      title: "Acceder - VaalaPyme",
      auth: false
    }
  },
  {
    name: "appointmentRequest",
    path: "/citas",
    component: AppointmentRequestComponent,
    meta: {
      auth: true
    }
  },
  {
    name: "appointmentRequestAct",
    path: "/citasac",
    component: appointmentRequestComponentAct,
    meta: {
      auth: true
    }
  },
  {
    name: "createUser",
    path: "/crearusuario",
    component: CreateUserComponent,
    meta: {
      auth: true
    }
  },
  {
    name: "employee",
    path: "/empleado",
    component: EmployeeComponent,
    meta: {
      auth: true
    }
  },
];

//const store = new Vuex.Store({});appointmentRequestComponentAct

const router = new VueRouter({
  mode: "history",
  routes: routes,
  base: ""
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
