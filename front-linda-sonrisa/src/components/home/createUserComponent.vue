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
              <input v-model="user.name" class="form-control form-control-sm" type="text"><br> 
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
              <input v-model="user.lastName" class="form-control form-control-sm" type="text"><br> 
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
              <input v-model="user.veryLastName" class="form-control form-control-sm" type="text"><br> 
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
              <input v-model="user.username" class="form-control form-control-sm" type="text"><br> 
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
              <input v-model="user.mail" class="form-control form-control-sm" type="mail"><br> 
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
              <select v-model="user.sex"  class="form-control form-control-sm">
                <option>Femenino</option>
                <option>Masculino</option>
                <option>Otro</option>
              </select>
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
              <input v-model="user.password" class="form-control form-control-sm" @input="passwordConfirm()" type="password"><br>
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
              <input v-model="user.passwordConfirm" class="form-control form-control-sm" @input="passwordConfirm()" type="password"><br>
              <label v-if="errPass != ''" class="text-danger text-sm">{{errPass}}</label>
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
    },
    errPass:'',
    
  }
},
mounted() {

},
methods:{
  Login(){
    
  },
  passwordConfirm(){
    this.errPass = '';
    if(this.user.password != this.user.passwordConfirm){
      if(this.user.password  == '' || this.user.passwordConfirm  == ''){
        this.errPass = '';
      }
      else{
        this.errPass = 'Su contraseña no es correcta';
      }
    }
    else{
      this.errPass = '';
    }
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
.text-sm{
  font-size: 14px;
}
</style>