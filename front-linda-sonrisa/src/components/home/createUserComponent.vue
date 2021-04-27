<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Bienvenido, Crea tu cuenta de usuario!!!</h1>
    </div>
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <div class="card text-center col-8 card-login">
        <div class="card-body flex-d justify-content-center">
          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Nombre:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.name"
                class="form-control form-control-sm"
                type="text"
              /><br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Rut:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.rut"
                class="form-control form-control-sm"
                type="text"
              /><br />
            </div>
          </div>
          <br />
          <!--
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
-->
          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Mail:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.email"
                class="form-control form-control-sm"
                type="mail"
              /><br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Contraseña:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.password"
                class="form-control form-control-sm"
                @input="passwordConfirm()"
                type="password"
              /><br />
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Confirmar contraseña:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.passwordConfirm"
                class="form-control form-control-sm"
                @input="passwordConfirm()"
                type="password"
              /><br />
              <label v-if="errPass != ''" class="text-danger text-sm">{{
                errPass
              }}</label>
            </div>
          </div>
          <br />
        </div>
        <div class="card-footer text-right">
          <button
            class="btn btn-sm btn-danger col-4"
            v-if="errPass == '' || user.password != user.passwordConfirm"
            disabled
          >
            Guardar usuario
          </button>
          <button
            class="btn btn-sm btn-secondary col-4"
            v-if="user.password == user.passwordConfirm"
            @click="storeUser()"
          >
            Guardar usuario
          </button>
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
        name: "",
        rut: "",
        username: "",
        email: "",
        password: "",
        passwordConfirm: "",
      },
      errPass: "",
      errors: "",
    };
  },
  mounted() {},
  methods: {
    Login() {},
    passwordConfirm() {
      this.errPass = "";
      if (this.user.password != this.user.passwordConfirm) {
        if (this.user.password == "" || this.user.passwordConfirm == "") {
          this.errPass = "";
        } else {
          this.errPass = "Su contraseña no es correcta";
        }
      } else {
        this.errPass = "";
      }
    },
    storeUser() {
      this.$axios
        .post("http://127.0.0.1:8000/api/usuarios", {
          name: this.user.name,
          email: this.user.email,
          password: this.user.password,
          rut: this.user.rut,
        })
        .then((response) => {
          console.log(response);
        })
        .catch((err) => {
          console.log("hola", err);
        });
    },
  },
};
</script>
<style>
.card-login {
  padding: 2px 3px;
}
.btn-sm {
  margin: 0px 12px;
  padding: 10px 0px;
}
.text-sm {
  font-size: 14px;
}
</style>

<!--
Error Code    : 1400Error Message 
: ORA-01400: cannot insert NULL into ("PORTAFOLIO"."USERS"."EMAIL") Position      : 102Statement 

: insert into "USERS" ("EMAIL", "PASSWORD", "ELIMINADO", "ROL_ID", "UPDATED_AT", "CREATED_AT") 
  values (:p0, :p1, :p2, :p3, :p4, :p5) returning "ID" into :p6Bindings  

: [,345345,1,1,2021-04-26 21:00:51,2021-04-26 21:00:51,0]
-->
