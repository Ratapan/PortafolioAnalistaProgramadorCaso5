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
      <div v-for="a in as" :key="a" class="card cardfrm" style="width: 18rem;">
        <img class="card-img-top" src="" alt="imagen">
        <div class="card-body">
          <h5 class="card-title">Nombre de profesional</h5>
          <p class="card-text">desc de prof.</p>
          <a href="#" class="btn btn-primary">pedir cita con este pro</a>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
data() {
  return {
    as: [1,2,3,4,5,6,7,8,9,10,11,12,13,14],
    search: '',
  }
},
}
</script>

<style>
.cardfrm{
  margin: 10px;
}
</style>