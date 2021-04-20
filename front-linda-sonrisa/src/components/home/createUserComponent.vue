<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Bienvenido, Crea tu cuenta de usuario!!!</h1>
    </div>
    <br>
    <br>
    <div class="row flex-d justify-content-around">
      <div class="card text-center col-8 card-login">
        <div class="card-body flex-d justify-content-center">


          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Nombre:</label>
            </div>
            <div class="col-5">
              <input v-model="user.name" type="text"><br> 
            </div>
          </div>
          <br>

          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Apellido paterno:</label>
            </div>
            <div class="col-5">
              <input v-model="user.lastName" type="text"><br> 
            </div>
          </div>
          <br>

          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Apellido materno:</label>
            </div>
            <div class="col-5">
              <input v-model="user.veryLastName" type="text"><br> 
            </div>
          </div>
          <br>

          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Nombre de usuario:</label>
            </div>
            <div class="col-5">
              <input v-model="user.username" type="text"><br> 
            </div>
          </div>
          <br>

          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Mail:</label>
            </div>
            <div class="col-5">
              <input v-model="user.mail" type="mail"><br> 
            </div>
          </div>
          <br>

          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Sexo:</label>
            </div>
            <div class="col-5">
              <input v-model="user.sex" type="text"><br> 
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
              <input v-model="user.password" type="password"><br>
          </div>
          </div>
          <br>
          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Confirmar contraseña:</label>
            </div>
            <div class="col-5">
              <input v-model="user.passwordConfirm" type="password"><br>
          </div>
          </div>
          <br>


        </div>
        <div class="card-footer text-right">
          <button class="btn btn-sm btn-secondary col-4" @click="Login()">Guardar usuario</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
data() {
  return {
    user: {
      name: '',
      lastName: '',
      veryLastName: '',
      username:'',
      mail:'',
      sex:'',
      password: '',
      passwordConfirm: '',
    }
    
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