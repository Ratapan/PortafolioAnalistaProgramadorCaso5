<template>
  <div id="app">
      <!--<router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>-->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Linda sonrisa</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" href="#">Pedir cita</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Citas activas</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Citas anteriores</a>
            </li>
          </ul>
          <ul class="navbar-nav d-flex justify-content-end">
            <li class="nav-item">
              <a class="nav-link" href="#">Entrar</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Crear cuenta</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Salir</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <br><br>
    <section class="pres">
      <presentation-component></presentation-component>
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

