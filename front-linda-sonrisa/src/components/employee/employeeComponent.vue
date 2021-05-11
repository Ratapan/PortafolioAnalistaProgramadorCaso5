<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Clinica odontologica Linda Sonrisa</h1>
    </div>
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <h3><strong> Horas </strong></h3>
    </div>
    <br />
    <!--
    <div class="row flex-d justify-content-around">
      <button type="button" class="btn btn-primary" @click="sta()">Agregar</button>
    </div>
    <br />
-->
    <div class="row flex-d justify-content-center">
      <br>
      <div class="col-4">
        <label>Dia</label>
      </div>
      <div class="col-6">
        <date-picker v-model="dayHour" valueType="format"></date-picker>
      </div>
      <br>
    </div>
    <br>
    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Hora inicial</label>
      </div>
      <div class="col-6">
        <date-picker
          v-model="iniHour"
          value-type="format"
          type="time"
          :open.sync="openI"
          placeholder="Selecciona hora de inicio"
          @change="handleChange"
        ></date-picker>
      </div>
      <br>
    </div>
    <br>
    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Hora de fin</label>
      </div>
      <div class="col-6">
        <date-picker
          v-model="endHour"
          value-type="format"
          type="time"
          :open.sync="openE"
          placeholder="Selecciona hora de fin"
          @change="handleChange"
        ></date-picker>
      </div>
      <br>
    </div>
    <br>
    <div class="row flex-d justify-content-center">
      <button type="button" class="btn btn-success" @click="sta(), storeHora()">Agregar hora</button>
    </div>
    <br />
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h3> Inicio hora </h3></th>
            <th scope="col"><h3>Fin hora</h3></th>
            <th scope="col"><h3>Acciones</h3></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="hora in horas" :key="hora">
            <td>{{hora.inicio_hora}}</td>
            <td>{{hora.fin_hora}}</td>
            <td>  
              <div class="row flex-d justify-content-around">
                <button type="button" class="btn btn-warning">Editar</button>
                <button type="button" class="btn btn-danger" @click="deleteHora(hora.id_hora)">Eliminar</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
      <br>
      <br>

  </div>
</template>

<script>
export default {
  data() {
    return{
      stat: false,
      openI: false,
      openE: false,
      dayHour:'',
      iniHour:'',
      endHour:'',
      horas:[],
    };
    
  },
  mounted() {
    this.getHoras();
  },
  methods: {
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },
    handleChange(value, type) {
      if (type === 'second') {
        this.open = false;
      }
    },
    storeHora() {
      this.$axios
        .post("http://127.0.0.1:8000/api/hora", {
          inicio:     `${this.dayHour} ${this.iniHour}`,
          fin:        `${this.dayHour} ${this.endHour}`,
          id_user:     this.$store.getters.value.id_user,
                      
        })
        .then((response) => {
          console.log(response);
          this.getHoras();
        })
        .catch((err) => {
          this.error = err;
        });
    },
    deleteHora(id){
      //
      console.log(id)

      this.$axios
        .post("http://127.0.0.1:8000/api/horad", {
          id:  id
        })
        .then((response) => {
          console.log(response)
          this.getHoras();
        })
        .catch((err) => {
          this.error = err;
        });
    },
    getHoras(){
      let page = 1
      this.$axios.
      get("http://127.0.0.1:8000/api/hora?page="+page).
      then(response => {
        this.horas = response.data.data;
        console.log(this.horas)
      });
    }

  }
};
</script>

<style></style>
