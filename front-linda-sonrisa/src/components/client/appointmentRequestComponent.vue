<template>
  <div class="container">
    <br />
    <section>
      <div class="row d-flex justify-content-around">
        <h1>Pedir citas</h1>
      </div>
      <br />
      <br />
      <div class="row d-flex justify-content-around">
        <div class="col-6">
          <input
            type="text"
            class="form-control form-control-sm"
            v-model="search"
          />
        </div>
      </div>
      <br /><br />

      <div class="row d-flex justify-content-around">
        <div
          v-for="se in services"
          :key="se.id_user"
          class="col-xl-2 col-lg-3 col-md-4 col-sm-6  d-flex justify-content-center  flex-column"
        >
          <button class="btn btn-primary fontbu">
            {{ upperCase(se.nombre_servicio) }}
          </button>
          <br />
          <p class="text-center">{{ se.precio }}</p>
        </div>
      </div>
      <br /><br />

      <div class="row d-flex justify-content-around">
        <div class="row d-flex justify-content-center">
          <div
            v-for="em in employees"
            :key="em.id_empleado"
            class="card cardfrm"
            style="width: 18rem;"
          >
            <img
              class="card-img-top"
              src="../../assets/dentist_icon.png"
              alt="imagen"
            />
            <div class="card-body">
              <h5 class="card-title">Nombre: {{ em.nombre_ape }}</h5>
              <p class="card-text">
                {{ em.tipo_cuenta }}
              </p>
              <button
                @click="empleado = em"
                v-b-modal.modal-1
                class="btn btn-primary"
              >
                Ver horas
              </button>
            </div>
          </div>
        </div>
      </div>

      <b-modal size="lg" id="modal-1" title="Horas" hide-footer>
        <cita-request-component :empleado="empleado" />
      </b-modal>

      <button class="btn btn-warning" v-b-modal.modal-help>
        Ayuda
      </button>
      <div>
        <b-modal size="lg" id="modal-help" class="modal" title="Ayuda" hide-footer>
          <help-component :info="info"></help-component>
          <br />
          <button class="btn btn-secondary" @click="$bvModal.hide('modal-help')">
            Cerrar
          </button>
          <br />
        </b-modal>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  data() {
    return {
      search: "",
      employees: {},
      empleado: {},
      services: {},
      info: [
        {
          title: "Pedir Cita",
          parr:
            "Si seleccionamos “Pedir Cita” se nos mostrarán los dentistas disponibles y podremos pedir una hora con este dentista.",
        },
        {
          title: "Ver horas",
          parr:
            "Al seleccionar “Ver horas”, podremos seleccionar un día para buscar horas disponibles.",
        },
        {
          title: "Seleccionando horas",
          parr:
            "Al seleccionar la hora ya tendremos lista nuestra cita con el dentista.",
        },
      ],
    };
  },
  mounted() {
    this.getEmp();
    this.getService();
    console.log(this.$store);
  },
  methods: {
    upperCase(str) {
      const stringUpper = str.charAt(0).toUpperCase() + str.slice(1);
      return stringUpper;
    },
    getEmp() {
      let page = 1;
      this.$axios
        .get("http://127.0.0.1:8000/api/employee?page=" + page)
        .then((response) => {
          this.employees = response.data.data;
          console.log(response.data.data);
        });
    },
    getService() {
      let page = 1;
      this.$axios
        .get("http://127.0.0.1:8000/api/servicio?page=" + page)
        .then((response) => {
          this.services = response.data.data;
          console.log(this.services);
        });
    },
  },
};
</script>

<style scoped>
.cardfrm {
  margin: 10px;
}
.fontbu {
  font-size: 20px;
  min-height: 60px;
}
</style>
