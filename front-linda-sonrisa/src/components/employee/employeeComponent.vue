<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Clinica odontologica Linda Sonrisa</h1>
    </div>
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <h3><strong> Horas </strong></h3>
    </div>
    <br />
    <div class="row flex-d justify-content-around">
      <button type="button" class="btn btn-primary" @click="sta()">Agregar</button>
    </div>
    <br />

    <div v-if="stat" class="row flex-d justify-content-center">
      <br>
      <div class="col-8 flex-d justify-content-around">
        <label>Dia</label>
        <date-picker v-model="dayHour" valueType="format"></date-picker>
      </div>
      <br>
      <div class="col-8">
        <label>Hora inicial</label>
        <vue-timepicker v-model="iniHour"></vue-timepicker>
      </div>
      <br>
      <div class="col-8">
        <label>Hora de fin</label>
        <vue-timepicker v-model="endHour"></vue-timepicker>
      </div>
      <br>
    </div>
    <div v-if="stat" class="row flex-d justify-content-center">
    <button v-if="stat"  type="button" class="btn btn-success" @click="sta()">Agregar hora</button>
    </div>

{{iniHour}}
{{endHour}}
    <br />
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h3> Inicio hora </h3></th>
            <th scope="col"><h3>Fin hora</h3></th>
            <th scope="col"><h3>Acciones</h3></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td scope="row">1</td>
            <td>Mark</td>
            <td>  
              <div class="row flex-d justify-content-around">
                <button type="button" class="btn btn-warning">Editar</button>
                <button type="button" class="btn btn-danger">Eliminar</button>
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
