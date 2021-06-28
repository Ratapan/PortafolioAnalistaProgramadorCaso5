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

// Main JS (in UMD format)
import VueTimepicker from 'vue2-timepicker'
// CSS
import 'vue2-timepicker/dist/VueTimepicker.css'
Vue.use(VueTimepicker)

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
//Help
import HelpComponent from "./views/help.vue";
Vue.component("help-component", HelpComponent );

import LoginComponent                 from "./components/home/loginComponent.vue";
import CreateUserComponent            from "./components/home/createUserComponent.vue";
import AppointmentRequestComponent    from "./components/client/appointmentRequestComponent.vue";
import appointmentRequestComponentAct from "./components/client/appointmentRequestComponentAct.vue";
import appointmentRequestComponentAnt from "./components/client/appointmentRequestComponentAnt.vue";
import appointmentRequestComponentBol from "./components/client/appointmentRequestComponentBol.vue";
import CitaRequestComponent           from "./components/client/citaComponent.vue";
import EmployeeComponent              from "./components/employee/employeeComponent.vue"
import EmployeeOrdenComponent         from "./components/employee/employeeOrdenComponent.vue"
import employeeRecepcionComponent     from "./components/employee/employeeRecepcionComponent.vue"
import employeeRecibidosComponent     from "./components/employee/employeeRecibidosComponent.vue"
import EmployeeAppointmentComponent   from "./components/employee/employeeAppointmentComponent.vue"
import ViewOrderComponent             from "./components/vendors/viewOrderComponent.vue"


Vue.component("employee-component",     EmployeeComponent );
Vue.component("cita-request-component", CitaRequestComponent );

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
    name: "appointmentRequestAnt",
    path: "/citasan",
    component: appointmentRequestComponentAnt,
    meta: {
      auth: true
    }
  },
  {
    name: "appointmentRequestBol",
    path: "/boletas",
    component: appointmentRequestComponentBol,
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
  {
    name: "employeeOrder",
    path: "/empleado/orden",
    component: EmployeeOrdenComponent,
    meta: {
      auth: true
    }
  },
  {
    name: "employeeRecepcion",
    path: "/empleado/recepcion",
    component: employeeRecepcionComponent,
    meta: {
      auth: true
    }
  },
  {
    name: "employeeRecibidos",
    path: "/empleado/recibidos",
    component: employeeRecibidosComponent,
    meta: {
      auth: true
    }
  },
  {
    name: "employeeAppointment",
    path: "/empleado/citas",
    component: EmployeeAppointmentComponent,
    meta: {
      auth: true
    }
  },
  {
    name: "viewOrders",
    path: "/ver/ordenes",
    component: ViewOrderComponent,
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
