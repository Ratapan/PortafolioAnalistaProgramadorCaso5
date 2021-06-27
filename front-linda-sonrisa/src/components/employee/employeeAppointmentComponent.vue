<template>
  <div class="container">
    <br>
    <div class="row flex-d justify-content-around">
      <h1>Citas</h1>
    </div>
    <br>
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th  scope="col"><h4>Día</h4></th>
            <th scope="col"><h4>Hora inicio y fin</h4></th>
            <th scope="col"><h4>Nombre</h4></th>
            <th scope="col"><h4>Estado</h4></th>
            <th scope="col"><h4>Acciones</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.id_cita" :class="statusColor(cita.estado)">
            <td>{{formDate(cita.inicio_hora)}}</td>
            <td>{{formHora(cita.inicio_hora)}}-{{formHora(cita.fin_hora)}}</td>
            <td>{{cita.nombre_ape}}</td>
            <td>{{status(cita.estado)}}</td>
            <td>  
              <div class="row flex-d justify-content-around">
                <button type="button" class="btn btn-primary" @click="verCita = cita" v-b-modal.modal-2>Ver</button>
                <button v-if="cita.estado == 'R'" type="button" class="btn btn-danger"  @click="cancelarHoraCita(cita.id_hora, cita.id_cita)">Cancelar</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
      <br>
      <br>


  <b-modal size="xl" id="modal-2" title="Datos de la cita" hide-footer>
    <br><br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Nombre paciente:</label>
      </div>
      <div class="col-7">
        <label>{{verCita.nombre_ape}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Rut:</label>
      </div>
      <div class="col-7">
        <label>{{verCita.rut}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Mail:</label>
      </div>
      <div class="col-7">
        <label>{{verCita.email}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Numero:</label>
      </div>
      <div class="col-7">
        <label>{{verCita.num_telefono_pa}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Servicios:</label>
      </div>
      <div class="col-7">
        <select v-model="servicio" class="form-select form-select-sm">
          <option v-for="tipo_servicio in servicios" :key="tipo_servicio.id_t_serv" :value="tipo_servicio.id_t_serv">{{ tipo_servicio.nombre_servicio }}</option>
        </select>
      </div>
    
    </div>
    <br>
    <button  v-if="verCita.estado == 'R' || verCita.estado == 'A' && servicio != ''" type="button" class="btn btn-danger" @click="terminarHoraCita(verCita.id_cita, $bvModal.hide('modal-2'))">Terminar</button>
    <button  v-if="verCita.estado == 'R'" type="button" class="btn btn-secondary"  @click="atrasadaHoraCita(verCita.id_cita, $bvModal.hide('modal-2'))">Atrasar</button>  
    <label class="text-danger" v-if="verCita.estado == 'R' || verCita.estado == 'A' && servicio == ''">Para terminar la cita es necesario elejir el servicio</label>
    <br>
  </b-modal>

  </div>
</template>

<script>
export default {
  data() {
    return{
      stat: false,
      openI: false,
      openE: false,
      dayHour:'',
      iniHour:'',
      endHour:'',
      user:  {},
      citas:[],
      verCita:[],
      servicios:[],
      servicio:'',
    };
    
  },
  mounted() {
    this.getCitaHoras();
    this.getService();
  },
  methods: {
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
    addZero(a, b) {
      if (a.toString().length < b) {
        return `0${a}`;
      } else {
        return `${a}`;
      }
    },
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },
    handleChange(value, type) {
      if (type === 'second') {
        this.open = false;
      }
    },
    getCitaHoras() {
      let page = 1;
      this.$axios
        .get(
          "http://127.0.0.1:8000/api/cita/hora?page=" +
            page 
            + "&id_user="
            + this.$store.getters.value.id_user
        )
        .then((response) => {
          this.citas = response.data.data;
          console.log(this.citas);
        });
    },
    cancelarHoraCita(ho,ci) {
      this.$axios
        .put("http://127.0.0.1:8000/api/cita/cancel", {
          id_hora:     ho,
          id_cita:     ci,
        })
        .then((response) => {
          this.getCitaHoras();
          this.$swal({
              title:
                "Hora cancelada.",
              icon: "success",
              timer: 3000,
              showConfirmButton: false
            });
          console.log(response);
        })
        .catch((err) => {
          this.error = err;
        });
    },
    terminarHoraCita(ci) {
      this.$axios
        .put("http://127.0.0.1:8000/api/cita/end", {
          id_cita:     ci,
          id_servicio: this.servicio,
        })
        .then((response) => {
          this.servicio='';
          this.getCitaHoras();
          this.$swal({
              title:
                "Cita terminada.",
              icon: "success",
              timer: 3000,
              showConfirmButton: false
            });
          console.log(response);
        })
        .catch((err) => {
          this.error = err;
        });
    },
    atrasadaHoraCita(ci) {
      this.$axios
        .post("http://127.0.0.1:8000/api/cita/atrasar", {
          id_cita:     ci,
        })
        .then((response) => {
          this.getCitaHoras();
          this.$swal({
              title:
                "Cita terminada.",
              icon: "success",
              timer: 3000,
              showConfirmButton: false
            });
          console.log(response);
        })
        .catch((err) => {
          this.error = err;
        });
    },
    aCita(ci) {
      this.$axios
        .put("http://127.0.0.1:8000/api/cita/end", {
          id_cita:     ci,
        })
        .then((response) => {
          this.getCitaHoras();
          this.$swal({
              title:
                "Cita terminada.",
              icon: "success",
              timer: 3000,
              showConfirmButton: false
            });
          console.log(response);
        })
        .catch((err) => {
          this.error = err;
        });
    },
    getService(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/servicio?page=" + page + 
            "&id_user=" + this.$store.getters.value.id_user).
            then(response => {
              this.servicios = response.data.data;
              console.log(this.servicios)
      });
    }
  }
};
</script>

<style scoped>
  button{
    margin-left:  3px;
    margin-right: 3px;
  }
</style>
