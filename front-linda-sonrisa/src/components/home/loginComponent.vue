<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Bienvenido</h1>
    </div>
    <br>
    <br>
    <div class="row flex-d justify-content-around">
      <div class="card text-center col-6 card-login">
        <div class="card-body flex-d justify-content-center">
          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Nombre de usuario:</label>
            </div>
            <div class="col-5">
              <input v-model="username" type="text"><br> 
            </div>
          </div>
          <br>
          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Contraseña:</label>
            </div>
            <div class="col-5">
              <input v-model="password" type="password"><br>
          </div>
          </div>
          <br>
        </div>
      
        <div class="card-footer text-right">
          <button class="btn btn-sm btn-secondary col-4" @click="Login()">Entrar</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
data() {
  return {
    username: '',
    password: '',
  }
},
mounted() {

},
methods:{
  Login(){
    
  }
}

}
</script>
<style>
.card-login{
  padding: 2px 3px;
}
.btn-sm{
  margin: 0px 12px;
  padding:10px 0px;
}
</style>