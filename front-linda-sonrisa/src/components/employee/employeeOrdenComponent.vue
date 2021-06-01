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
        <select v-model="family" class="form-select form-select-sm">
          <option v-for="tipo_servicio in servicios" :key="tipo_servicio" :value="tipo_servicio.id_t_serv">{{ tipo_servicio.nombre_servicio }}</option>
        </select>
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
      family :{},
      product:{},

      familys :{},
      products:{},
    };
  },
  watch: {
    family(){
      this.getProduct();
    }
  },
  mounted() {
    this.getFamily();
    this.getProduct();
  },
  methods: {
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },
    getFamily(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/familia?page=" + page).
            then(response => {
              this.familys = response.data.data;
              console.log(this.familys)
      });
    },
    getProduct(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/producto?page=" + page).
            then(response => {
              this.products = response.data.data;
              console.log(this.products)
      });
    },


  }
};
</script>

<style></style>
