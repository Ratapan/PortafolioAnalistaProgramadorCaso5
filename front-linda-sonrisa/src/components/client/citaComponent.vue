<template>
  <div class="container">
    <br>
    <div class="row flex-d justify-content-center">
      <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 d-flex justify-content-center list">
        <date-picker
          v-model="dateO"
          value-type="format"
          placeholder="Selecciona hora de fin"
        ></date-picker>
      </div>
      <br>
      <br>
      <br>
      <!--<div class="col-xl-4 col-lg-4 col-md-4 col-sm-4">
        <ul class="list-group">
          <li class="list" v-for="hora in horas" :key="hora">
            <button class="btn btn-info" @click="storeHora(hora.id_hora)">
              {{ btnHora(hora.inicio_hora) }} | {{ btnHora(hora.fin_hora) }}
            </button>
          </li>
        </ul>
      </div>-->
      <div class="col-xl-3 col-lg-3 col-md-4 col-sm-4 d-flex justify-content-center list"  v-for="(hora,i) in horas" :key="i.id_hora">
        <br>
        <button class="btn btn-info" @click="storeHora(hora.id_hora)">
          {{ btnHora(hora.inicio_hora) }} | {{ btnHora(hora.fin_hora) }}
        </button>
      </div>
    </div>
    <br><br>
    <button class="btn btn-warning" v-b-modal.modal-helpp>
        Ayuda
      </button>
      <div>
        <b-modal size="lg" id="modal-helpp" class="modal" title="Ayuda" hide-footer>
          <help-component :info="info"></help-component>
          <br />
          <button class="btn btn-secondary" @click="$bvModal.hide('modal-helpp')">
            Cerrar
          </button>
          <br />
        </b-modal>
      </div>
  </div>
</template>

<script>
export default {
  props: {
    empleado: {
      type: Object,
    },
  },
  data() {
    return {
      stat: false,
      openI: false,
      openE: false,
      dayHour: "",
      iniHour: "",
      endHour: "",
      horas: [],
      dateO: "",
      info: [
        {title: "Seleccionando horas", parr: "Al seleccionar la hora ya tendremos lista nuestra cita con el dentista."},
      ],
    };
  },
  mounted() {},
  watch: {
    empleado: function() {
      this.getHoras();
    },
    dateO: function() {
      this.getHoras();
    },
  },
  methods: {
    btnHora(hr) {
      let hora = new Date(hr);
      return `${this.addZero(hora.getHours(), 2)}:${this.addZero(hora.getMinutes(),2)}`;
    },
    addZero(a, b) {
      if (a.toString().length < b) {
        return `0${a}`;
      } else {
        return `${a}`;
      }
    },
    sta() {
      if (this.stat == false) {
        this.stat = true;
      } else {
        this.stat = false;
      }
    },
    logout() {
      this.$store.getters.value = null;
    },
    handleChange(value, type) {
      if (type === "second") {
        this.open = false;
      }
    },
    storeHora(id) {

      console.log(id);
      let diaHoy = new Date();
      this.$axios
        .post("http://127.0.0.1:8000/api/cita", {
          fecha: `${this.addZero(diaHoy.getFullYear(), 2)}-${this.addZero(
            diaHoy.getMonth(),
            2
          )}-${this.addZero(diaHoy.getDate(), 2)} ${this.addZero(
            diaHoy.getHours(),
            2
          )}:${this.addZero(diaHoy.getMinutes(), 2)}:${this.addZero(
            diaHoy.getSeconds(),
            2
          )}`,
          id_hora: id,
          id_user: this.$store.getters.value.id_user,

        })
        .then((response) => {
          console.log(response);
          this.getHoras();
        })
        .catch((err) => {
          this.error = err;
        });
    },
    getHoras() {
      let page = 1;
      this.$axios
        .get(
          "http://127.0.0.1:8000/api/hora-d?page=" +
            page 
            +"&id_emp=" 
            +this.empleado.id_empleado
            +"&fini="
            +`${this.dateO} 00:00:00`
            +"&ffin="
            +`${this.dateO} 23:59:50`
        )
        .then((response) => {
          this.horas = response.data.data;
          console.log(this.horas);
        });
    },
  },
};
</script>

<style scoped>
.list {
  list-style: none;
  margin: 15px 0px;
}
.space {
  margin: 30px 10px;
}
</style>
