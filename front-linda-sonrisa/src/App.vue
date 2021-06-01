<template>
  <div id="app">
      <!--<router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>-->
    <nav class="navbar navbar-expand-lg sticky-top navbar-light bg-light">
      
        <a class="navbar-brand">Linda sonrisa</a>
        
        <button 
          class="navbar-toggler" 
          type="button" 
          data-bs-toggle="collapse" 
          data-bs-target="#navbarNav" 
          aria-controls="navbarNav" 
          aria-expanded="false"
          aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'init' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Home
                </router-link>
            </li>
          </ul>
          <ul class="navbar-nav" v-if="$store.getters.value != null && $store.getters.value.roles_id_rol == 1">
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'appointmentRequest' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Pedir cita
                </router-link>
            </li>
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'appointmentRequestAct' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Citas activas
                </router-link>
            </li>
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'appointmentRequestAnt' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Citas inactivas
                </router-link>
            </li>
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'appointmentRequestBol' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Boletas
                </router-link>
            </li>
          </ul>
          <ul class="navbar-nav" v-if="$store.getters.value != null && $store.getters.value.roles_id_rol == 3">
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'employee' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Horas
                </router-link>
            </li>
            <li class="nav-item">
              <router-link 
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'employeeAppointment' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Citas
                </router-link>
            </li>
            <li class="nav-item">
              <router-link 
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'employeeOrder' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Realizar orden
                </router-link>
            </li>
          </ul>
          <ul class="navbar-nav" v-if="$store.getters.value != null && $store.getters.value.roles_id_rol == 4">
            <li class="nav-item">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'viewOrders' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Revisar ordenes
                </router-link>
            </li>
          </ul>
          <ul class="navbar-nav ml-auto">
            <li class="nav-item"  v-if="$store.getters.value == null">
              <router-link 
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'login' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Entrar
              </router-link>
            </li>
            <li class="nav-item" v-if="$store.getters.value == null ">
              <router-link
                  class="nav-link dropdown-item dropdown-item-pyme"
                  style="border-radius: 500px"
                  :to="{ name: 'createUser' }"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarNav"
                  aria-expanded="false"
                >
                  Crear cuenta
              </router-link>
            </li>
            <li class="nav-item ">
              <a class="nav-link dropdown-item" @click="logout()" v-if="$store.getters.value != null ">Salir</a>
            </li>
          </ul>
        </div>
      
    </nav>
    <br>
    <section class="pres">
      <!-- <section v-if="$store.getters.value != null">{{$store.getters.value.rol_id}}</section> -->
    <transition name="fade" mode="out-in">
      <router-view></router-view>
    </transition>
    </section>
    <br>
    <footer-component class="footer"></footer-component>
  </div>
</template>
<script>
export default {
  methods: {
    logout() {
      console.log("1",this.$store.getters) 
      this.$store.getters.value = null; 
      console.log("2",this.$store.getters.value) 
      console.log("3",this.$store.getters) 
      this.$router.push("/acceder");
    }
  }
};
</script>

<style scoped>
html, body{
  font-family: 'Montserrat', sans-serif;
  }
.navbar-nav .nav-link {
    padding-right:0;
    padding-left: 0;
}
.pres{
  min-height: 800px;
  }
.footer {
  padding: 10px 10px 0px 10px;
  bottom: 0;
  width: 100%;
  background: grey;
}
.nav{
  position: -webkit-sticky;
  position: sticky;
  top: 0px;
}
template{
  min-height:100%;
}
</style>