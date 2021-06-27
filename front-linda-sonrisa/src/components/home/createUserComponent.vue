<template>
  <div class="container">
    <br>
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
                placeholder="12787434-3"
                class="form-control form-control-sm"
                type="text"
              /><br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Dirección:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.direction"
                placeholder="AV.siempre viva 123"
                class="form-control form-control-sm"
                type="text"
              /><br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Fecha de nacimiento:</label>
            </div>
            <div class="col-5">
              <date-picker v-model="user.birthDate" 
              valueType="format"></date-picker><br/>
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Numero movil:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.phone"
                placeholder="+56954624984"
                class="form-control form-control-sm"
                type="text"
              /><br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Salud:</label>
            </div>
            <div class="col-5">

              <select   v-model="user.salud" class="form-control form-control-sm">
                <option value="0" disabled>Seleccione</option>
                <option value="1">Fonasa</option>
                <option value="2">Isapre</option>
              </select>

              <br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Documento de situación económica:</label>
            </div>
            <div class="col-5">
              <input
                class="form-control-file"
                type="file" id="file" ref="file"
                @change="handleFileUpload()"
              /><br />
            </div>
          </div>
          <br />

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Mail:</label>
            </div>
            <div class="col-5">
              <input
                placeholder="juanpedro@gmail.com"
                v-model="user.email"
                class="form-control form-control-sm"
                type="mail"
              /><br />
            </div>
          </div>
          <br/>

          <div class="row">
            <div class="col-1"></div>
            <div class="col-5 text-left">
              <label>Contraseña:</label>
            </div>
            <div class="col-5">
              <input
                v-model="user.password"
                placeholder="8 dígitos"
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
        <div class="row flex-d justify-content-center">
          <div v-if= "error != ''" class="alert alert-danger col-xl-8 col-lg-8 col-md-9 col-sm-9" role="alert">
              {{ error }}
          </div>
        </div>
        <div class="card-footer text-right">
          <button
            class="btn btn-sm btn-danger col-4"
            v-if="user.password != user.passwordConfirm && user.salud != 0"
            disabled
          >
            Guardar usuario
          </button>
          <button
            class="btn btn-sm btn-secondary col-4"
            v-if="   user.password  ==  user.passwordConfirm
                  && user.name      !=  ''     
                  && user.password  !=  ''     
                  && user.rut       !=  ''   
                  && user.email     !=  ''
                "
            @click="storeUser()"
          >
            Guardar usuario
          </button>  
        </div>
      </div>
    </div>
    <br>
    <br>
    <br>

    <br>
  </div>
</template>

<script>
export default {
  data() {
    return {
      files: '',
      user: {
        name: "",
        rut: "",
        username: "",
        direction: "",
        birthDate: "",
        phone: "",
        salud: 0,
        document: "",
        email: "",   
        password: "",
        passwordConfirm: "",
      },
      errPass: "",
      error: [],
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
          nombre:    this.user.name,
          direccion: this.user.direction,
          fecha:     this.user.birthDate,
          telefono:  this.user.phone,
          salud:     this.user.salud,
          documento: this.user.document,
          email:     this.user.email,
          password:  this.user.password,
          rut:       this.user.rut,
        })
        .then((response) => {
          console.log(response);
          this.$router.push("/");
        })
        .catch((err) => {
          this.error = err;
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
.mx-input {
  display: block;
  width: 100%;
  font-size: 1rem;
  color: #495057;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid #ced4da;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;



  height: calc(1.5em + 0.5rem + 2px);
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
  line-height: 1.5;
  border-radius: 0.2rem;
}
</style>

<!--
Error Code    : 1400Error Message 
: ORA-01400: cannot insert NULL into ("PORTAFOLIO"."USERS"."EMAIL") Position      : 102Statement 

: insert into "USERS" ("EMAIL", "PASSWORD", "ELIMINADO", "ROL_ID", "UPDATED_AT", "CREATED_AT") 
  values (:p0, :p1, :p2, :p3, :p4, :p5) returning "ID" into :p6Bindings  

: [,345345,1,1,2021-04-26 21:00:51,2021-04-26 21:00:51,0]
-->
