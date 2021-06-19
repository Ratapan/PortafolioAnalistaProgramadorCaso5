<template>
  <div class="container">
    <br>
    <div class="row flex-d justify-content-around">
      <h1>Productos Recibidos</h1>
    </div>
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h3>Codigo</h3></th>
            <th scope="col"><h3>Comentario</h3></th>
            <th scope="col"><h3>Precio Total</h3></th>
            <th scope="col"><h3>Fecha de Vencimiento</h3></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="recepcion in recepciones" :key="recepcion.id_recepcion">
            <td>{{recepcion.codigo}}</td>
            <td>{{recepcion.comentario}}</td>
            <td>{{recepcion.precio_total}}</td>
            <td>{{recepcion.fecha_venc_o}}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return{
      recepciones :{},
    };
  },
  mounted() {
    this.getRecepciones();
  },
  methods: {
    getRecepciones(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/productosRecibidos?page=" + page).
            then(response => {
              this.recepciones = response.data.data;
              //console.log(this.recepciones)
      });
    },
  }
}
</script>

<style>

</style>