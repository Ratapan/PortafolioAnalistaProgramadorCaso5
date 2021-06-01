<template>
  <div class="container">
    <div class="row d-flex justify-content-around">
        <h1>Citas Activas</h1>
      </div>
      <br/>
      <br/>
      <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h4>Fecha</h4></th>
            <th scope="col"><h4>Nombre de dentista</h4></th>
            <th scope="col"><h4>Acciones</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.id_cita">
            <td>{{formDate(cita.inicio_hora)}} {{formHora(cita.inicio_hora)}}</td>
            <td>{{cita.nombre_ape}}</td>
            <td>
              <button type="button" class="btn btn-danger" @click="deleteCita(cita.id_cita,cita.horas_id_hora)">Cancelar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
data() {
  return {
    search: '',
    citas:{},
  }
},
mounted() {
    this.getCitas();
  },
methods:{
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
    get("http://127.0.0.1:8000/api/citas?page=" 
    + page 
    + "&id_user=" 
    + this.$store.getters.value.id_user
    + "&fecha=" 
    + `${this.addZero(diaHoy.getFullYear(), 2)}-
        ${this.addZero(diaHoy.getMonth(),2)}-
        ${this.addZero(diaHoy.getDate(), 2)} 
    00:00:00`).
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
  deleteCita(id_cita,id_hora){
      //
      console.log(id_cita,id_hora)

      this.$axios
        .post("http://127.0.0.1:8000/api/cita/cancel", {
          id_cita:  id_cita,
          id_hora:  id_hora
        })
        .then((response) => {
          console.log(response)
          this.getCitas();
        })
        .catch((err) => {
          this.error = err;
        });
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