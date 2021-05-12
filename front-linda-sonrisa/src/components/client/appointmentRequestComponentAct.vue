<template>
  <div class="container">
    <button @click="getCitas()">hola</button>
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
            <th scope="col"><h4>Acciones</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita">
            <td>{{cita.fecha_solicitacion}}</td>
            <td>
              <h4><i class="fa fa-trash" aria-hidden="true"></i></h4>
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