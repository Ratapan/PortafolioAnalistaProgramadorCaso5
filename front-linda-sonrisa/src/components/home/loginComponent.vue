<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Bienvenido</h1>
    </div>
    <br>
    <br>
    <div class="row flex-d justify-content-around">
      <div class="card text-center  col-xl-6 col-lg-7 col-md-9 col-sm-12 card-login">
        <div class="card-body flex-d justify-content-center">
          <div class="row">
            <div class="col-1">
            </div>
            <div class="col-5 text-left">
              <label>Correo:</label>
            </div>
            <div class="col-5">
              <input v-model="email" type="email"><br> 
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
          
        </div>
        <div class="row flex-d justify-content-center">
          <div v-if= "error != ''" class="alert alert-danger col-xl-8 col-lg-8 col-md-9 col-sm-9" role="alert">
              {{ error }}
          </div>
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
    email: '',
    password: '',
    error:'',
  }
},
mounted() {

},
methods:{
  Login(){
    this.$axios
        .post("http://127.0.0.1:8000/api/login", {
          email: this.email,
          password: this.password
        })
        .then(response => {
          console.log(response.data)
          this.$store.getters = {
            key: "user",
            value: response.data
          };
          //console.log(this.$store)
          if(this.$store.getters.value.rol_id == 1){this.$router.push("/");}
          if(this.$store.getters.value.rol_id == 2){this.$router.push("/");}
          if(this.$store.getters.value.rol_id == 3){this.$router.push("/");}
        })
        .catch(error => {
          this.error = error.response.data;
        });
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