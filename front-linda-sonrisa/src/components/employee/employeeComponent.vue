<template>
  <div class="container">
    <br>
    <div class="row flex-d justify-content-around">
      <h1>Crear horas</h1>
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
            <th scope="col"><h3> Día </h3></th>
            <th scope="col"><h3>Inicio y fin de hora</h3></th>
            <th scope="col"><h3>Estado</h3></th>
            <th scope="col"><h3>Acciones</h3></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="hora in horas" :key="hora.id_hora" :class="statusColor(hora.estado_hora)">
            <td>{{formDate(hora.inicio_hora)}}</td>
            <td>{{formHora(hora.inicio_hora)}}-{{formHora(hora.fin_hora)}}</td>
            <td>{{hourState(hora.estado_hora)}}</td>
            <td>  
              <div class="row flex-d justify-content-around">
                <!--<button type="button" class="btn btn-warning">Editar</button>-->
                <button v-if="hora.estado_hora =='D'" type="button" class="btn btn-danger" @click="deleteHora(hora.id_hora)">Eliminar</button>
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
    formHora(hr) {
      let hora = new Date(hr);
      return `${this.addZero(hora.getHours(), 2)}:${this.addZero(hora.getMinutes(),2)}`;
    },
    formDate(dt) {
      let date = new Date(dt);
      return `${this.addZero(date.getDate(), 2)}/${this.addZero(date.getMonth()+1,2)}`;
    },
    addZero(a, b) {
      if (a.toString().length < b) {
        return `0${a}`;
      } else {
        return `${a}`;
      }
    },
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    hourState(st){
      if(st == "D"){
        return "Disponible"
      }else{
        return "Tomada"
      }
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
    },
    statusColor(st){
      if(st == "T"){
        return "table-success"
      }
      if(st == "D"){
        return "table-light"
      }
    },

  }
};
</script>

<style></style>
