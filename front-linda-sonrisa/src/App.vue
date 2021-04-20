<template>
  <div id="app">
      <!--<router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>-->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="/">Linda sonrisa</a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
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
          <ul class="navbar-nav">
            <li class="nav-item">
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
            <li class="nav-item">
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
            <li class="nav-item">
              <a class="nav-link" href="#">Salir</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <br>
    <section class="pres">

    <transition name="fade" mode="out-in">
      <router-view></router-view>
    </transition>
    
    </section>
    <br><br>
    <footer-component></footer-component>
  </div>
</template>

<style>
html, body{
  font-family: 'Montserrat', sans-serif;
  }
.pres{
  min-height: 500px;
  }
</style>
<style scoped>
template{
  min-height:100%;
}
</style>

