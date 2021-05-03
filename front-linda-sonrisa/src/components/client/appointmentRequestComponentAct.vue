<template>
  <div class="container">
    <div class="row d-flex justify-content-around">
      <div v-for="se in services" :key="se" class="col-xl-2 col-lg-3 col-md-4 col-sm-6  d-flex justify-content-center  flex-column">
        <button class="btn btn-primary fontbu">{{upperCase(se.nombre_servicio)}}</button>
        <br>
        <p class="text-center">{{se.precio}}</p>
      </div>
    </div>
    <br><br>


  </div>
</template>

<script>
export default {
data() {
  return {
    search: '',
    employees:{},
    services:{},
  }
},
mounted() {
    this.getEmp();
    this.getService();
    console.log(this.$store)
  },
methods:{
  upperCase(str){
    const stringUpper = str.charAt(0).toUpperCase() + str.slice(1);
    return stringUpper;
  },
  getEmp(){
    let page = 1
    this.$axios.
    get("http://127.0.0.1:8000/api/employee?page=" + page).
    then(response => {
      this.employees = response.data.data;
      console.log(this.employees)
    });
  },
  getService(){
    let page = 1
    this.$axios.
    get("http://127.0.0.1:8000/api/servicio?page=" + page).
    then(response => {
      this.services = response.data.data;
      console.log(this.services)
    });
  }
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