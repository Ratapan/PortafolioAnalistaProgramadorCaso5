<template>
  <div id="app">
      <!--<router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>-->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
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
          <ul class="navbar-nav" v-if="$store.getters.value != null && $store.getters.value.rol_id == 1">
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
              <a class="nav-link" href="#">Citas activas</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Citas anteriores</a>
            </li>
          </ul>
          <ul class="navbar-nav" v-if="$store.getters.value != null && $store.getters.value.rol_id == 2">
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
                  Realizar orden
                </router-link>
            </li>
          </ul>
          <ul class="navbar-nav" v-if="$store.getters.value != null && $store.getters.value.rol_id == 3">
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
              <label class="nav-link nav-link dropdown-item" @click="logout()" v-if="$store.getters.value != null ">Salir</label>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <br>
    <section class="pres">
      <section v-if="$store.getters.value != null">{{$store.getters.value.rol_id}}</section>

    <transition name="fade" mode="out-in">
      <router-view></router-view>
    </transition>
    
    </section>
    <br><br>
    <footer-component></footer-component>
  </div>
</template>
<script>
export default {
  methods: {
    logout() {
      this.$store.getters = null
    }
  }
};
</script>

<style scoped>
html, body{
  font-family: 'Montserrat', sans-serif;
  }
.pres{
  min-height: 500px;
  }
template{
  min-height:100%;
}
</style>

