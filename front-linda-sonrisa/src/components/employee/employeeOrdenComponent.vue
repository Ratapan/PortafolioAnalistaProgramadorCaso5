<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Ordenes</h1>
    </div>
    <br />
    <div class="row flex-d justify-content-around">
      <h3><strong> Crear orden </strong></h3>
    </div>
    <br />
    <!--
    <div class="row flex-d justify-content-around">
      <button type="button" class="btn btn-primary" @click="sta()">Agregar</button>
    </div>
    <br />
-->
    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Tipo productos</label>
      </div>
      <div class="col-6">
        <input class="form-control form-control-sm" type="text">
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Productos</label>
      </div>
      <div class="col-6">
        <input class="form-control form-control-sm" type="text">
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>N de productos</label>
      </div>
      <div class="col-6">
        <input class="form-control form-control-sm" type="text">
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center">
      <br>
      <div class="col-4">
        <label>Fecha de vencimiento</label>
      </div>
      <div class="col-6">
        <date-picker v-model="dayHour" valueType="format"></date-picker>
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Fecha</label>
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
      <button type="button" class="btn btn-success" @click="sta(), storeHora()">Agregar hora</button>
    </div>
    <br />
    <br />
  </div>
</template>

<script>
export default {
  data() {
    return{
      stat: false,
      dayHour:'',
      iniHour:'',
      endHour:'',
    };
  },
  methods: {
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },


  }
};
</script>

<style></style>
