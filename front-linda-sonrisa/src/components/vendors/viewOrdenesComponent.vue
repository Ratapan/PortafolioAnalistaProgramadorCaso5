<template>
  <div class="container">
    <br>
    <div class="row flex-d justify-content-around">
      <h1>Ordenes</h1>
    </div>
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h3>Producto</h3></th>
            <th scope="col"><h3>Cantidad</h3></th>
            <th scope="col"><h3>Estado</h3></th>
            <th scope="col"><h3>Acciones</h3></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="orden in ordenes" :key="orden.id_orden" :class="statusColor(orden.estado)">
            <td>{{orden.nombre_tipop}}</td>
            <td>{{orden.cant_productos}}</td>
            <td>{{status(orden.estado)}}</td>
            <td>  
              <div class="row flex-d justify-content-around">
                <button type="button" class="btn btn-primary" @click="ordeness = orden" v-b-modal.modal>Ver Mas información</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
      <br>
      <br>

    <b-modal size="xl" id="modal" title="Datos de la orden" hide-footer>
    <br><br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Familia del productos:</label>
      </div>
      <div class="col-7">
        <label>{{ordeness.nombre_tipo_familia}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Producto:</label>
      </div>
      <div class="col-7">
        <label>{{ordeness.nombre_tipop}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Cantidad:</label>
      </div>
      <div class="col-7">
        <label>{{ordeness.cant_productos}}</label>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-1"></div>
      <div class="col-3 text-left">
        <label>Valor Total:</label>
      </div>
      <div class="col-7">
        <label>{{ordeness.precio_total}}</label>
      </div>
    </div>
    <br>
  </b-modal>
  </div>
</template>

<script>
export default {
  data() {
    return{
      ordenes :{},
      ordeness :[],
    };
  },
  mounted() {
    this.getOrdenes();
  },
  methods: {
    status(st){
      if(st == "R"){
        return "Rechazada"
      }
      if(st == "A"){
        return "Aceptada"
      }
      if(st == "E"){
        return "Entregada"
      }
    },
    statusColor(st){
      if(st == "R"){
        return "table-danger"
      }
      if(st == "E"){
        return "table-success"
      }
      if(st == "A"){
        return "table-primary"
      }
    },
    getOrdenes(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/ordenes/AR?page=" + page + 
            "&id_user=" + this.$store.getters.value.id_user).
            then(response => {
              this.ordenes = response.data.data;
              //console.log(this.ordenes)
      });
    },
    storeOrden(id) {
      this.$axios
        .post("http://127.0.0.1:8000/api/ordenes/aceptar", {
          id_orden:    id,
          fecha_v:     this.dayHour,
        })
        .then((response) => {
          console.log(response);
          this.$router.push("/");
        })
        .catch((err) => {
          this.error = err;
          console.log("hola", err);
        });
    },
    cancelarOrden(id) {
      this.$axios
        .post("http://127.0.0.1:8000/api/ordenes/rechazar", {
          id_orden:    id,
        })
        .then((response) => {
          console.log(response);
          this.$router.push("/");
        })
        .catch((err) => {
          this.error = err;
          console.log("hola", err);
        });
    },
  }
}
</script>

<style>

</style>