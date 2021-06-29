<template>
  <div class="container"><br>
    <div class="row d-flex justify-content-around">
        <h1>Citas inactivas</h1>
      </div>
      <br/>
      <br/>
      <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h4>Fecha</h4></th>
            <th scope="col"><h4>Nombre de dentista</h4></th>
            <th scope="col"><h4>Estado</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.id_cita" :class="statusColor(cita.estado)">
            <td>{{formDate(cita.inicio_hora)}} {{formHora(cita.inicio_hora)}}</td>
            <td>{{cita.nombre_ape}}</td>
            <td>{{status(cita.estado)}}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <button class="btn btn-warning" v-b-modal.modal-help>
        Ayuda
      </button>
      <div>
        <b-modal size="lg" id="modal-help" class="modal" title="Ayuda" hide-footer>
          <help-component :info="info"></help-component>
          <br />
          <button class="btn btn-secondary" @click="$bvModal.hide('modal-help')">
            Cerrar
          </button>
          <br />
        </b-modal>
      </div>
  </div>
</template>

<script>
export default {
data() {
  return {
    search: '',
    citas:{},
      info: [
        {title: "Citas Inactivas", parr: "Si seleccionamos “Citas Inactivas” se nos mostrarán las citas que ya terminamos o cancelamos, junto con la fecha y hora, el nombre del dentista y el estado de estas."},

      ],
  }
},
mounted() {
    this.getCitas();
  },
methods:{
  status(st){
      if(st == "R"){
        return "Reservada"
      }
      if(st == "C"){
        return "Cancelada"
      }
      if(st == "T"){
        return "Terminada"
      }
      if(st == "A"){
        return "Atrasada"
      }
    },
    statusColor(st){
      if(st == "R"){
        return "table-light"
      }
      if(st == "C"){
        return "table-danger"
      }
      if(st == "T"){
        return "table-success"
      }
      if(st == "A"){
        return "table-dark"
      }
    },
    formHora(hr) {
      let hora = new Date(hr);
      return `${this.addZero(hora.getHours(), 2)}:${this.addZero(hora.getMinutes(),2)}`;
    },
    formDate(dt) {
      let date = new Date(dt);
      return `${this.addZero(date.getDate(), 2)}/${this.addZero(date.getMonth()+1,2)}`;
    },
  upperCase(str){
    const stringUpper = str.charAt(0).toUpperCase() + str.slice(1);
    return stringUpper;
  },
  getCitas(){
    let page = 1
    let diaHoy = new Date();
    this.$axios.
    get("http://127.0.0.1:8000/api/citas/ant?page=" 
    + page 
    + "&id_user=" 
    + this.$store.getters.value.id_user
    + "&fecha=" 
    + `${this.addZero(diaHoy.getFullYear(), 2)}-
        ${this.addZero(diaHoy.getMonth(),2)}-
        ${this.addZero(diaHoy.getDate(), 2)} 
    00:00:01`).
      then(response => {
        this.citas = response.data.data;
        console.log(this.citas)
      }
    );
  },
  addZero(a, b) {
    if (a.toString().length < b) {
      return `0${a}`;
    } else {
      return `${a}`;
    }
  },
}
}
</script>

<style scoped>
.cardfrm{
  margin: 10px;
}
.fontbu{
  font-size:20px;
  min-height:60px;
}
</style>