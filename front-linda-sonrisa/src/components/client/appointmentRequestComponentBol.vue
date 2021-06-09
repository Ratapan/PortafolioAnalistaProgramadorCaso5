<template>
  <div class="container"><br>
    <div class="row d-flex justify-content-around">
        <h1>Boletas</h1>
      </div>
      <br/>
      <br/>
      <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h4>Fecha</h4></th>
            <th scope="col"><h4>Dentista</h4></th>
            <th scope="col"><h4>Servicio</h4></th>
            <th scope="col"><h4>Valor</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="boleta in boletas" :key="boleta">
            <td>{{formDate(boleta.fin_hora)}}  {{formHora(boleta.inicio_hora)}}-{{formHora(boleta.fin_hora)}}</td>
            <td>{{boleta.nombre_ape}}</td>
            <td>{{boleta.nombre_servicio}}</td>
            <td>{{boleta.precio}}</td>
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
    boletas:[],
  }
},
mounted() {
    this.getBoletas();
  },
methods:{
    formHora(hr) {
      let hora = new Date(hr);
      return `${this.addZero(hora.getHours(), 2)}:${this.addZero(hora.getMinutes(),2)}`;
    },
    formDate(dt) {
      let date = new Date(dt);
      return `${this.addZero(date.getDate(), 2)}/${this.addZero(date.getMonth()+1,2)}/${date.getFullYear()}`;
    },
  upperCase(str){
    const stringUpper = str.charAt(0).toUpperCase() + str.slice(1);
    return stringUpper;
  },
  getBoletas(){
    let page = 1
    this.$axios.
    get("http://127.0.0.1:8000/api/boletas?page=" 
    + page 
    + "&id_user=" 
    + this.$store.getters.value.id_user
    ).
      then(response => {
        this.boletas = response.data.data;
        console.log(this.boletas)
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