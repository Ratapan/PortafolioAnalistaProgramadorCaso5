<template>
  <div class="container">
    <div class="row d-flex justify-content-around">
      <h1>Pedir citas (usuario logeado)</h1>
    </div><br>

    <div class="row d-flex justify-content-around">
      <div class="col-6">
        <input type="text" class="form-control form-control-sm" v-model="search">
      </div>
    </div>
    <br><br>
    <div class="row d-flex justify-content-around">
      <div v-for="em in employees" :key="em" class="card cardfrm" style="width: 18rem;">
        <img class="card-img-top" src="../../assets/dentist_icon.png" alt="imagen">
        <div class="card-body">
          <h5 class="card-title">Nombre: {{em.nombre_e}}</h5>
          <p class="card-text">desc de prof.</p>
          <a href="#" class="btn btn-primary">Rut: {{em.rut_e}}</a>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
data() {
  return {
    search: '',
    employees:{},
  }
},
mounted() {
    this.getEmp();
  },
methods:{
  getEmp(){
    let page = 1
    this.$axios.
    get("http://127.0.0.1:8000/api/employee?page=" + page).
    then(response => {
      this.employees = response.data.data;
      console.log(this.employees)
    });
  }
}
}
</script>

<style>
.cardfrm{
  margin: 10px;
}
</style>