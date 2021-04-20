import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'


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

//const routes = [
//  {
//    name: "init",
//    path: "/",
//    component: LandingComponent,
//    meta: {
//      title: "Linda sonrisa",
//    auth: false
//    }
//  }
//]

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
