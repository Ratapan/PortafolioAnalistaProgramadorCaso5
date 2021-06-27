<template>
  <div class="container">
    <br />
    <div class="row d-flex justify-content-around">
      <h1>Boletas</h1>
    </div>
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h4>Fecha</h4></th>
            <th scope="col"><h4>Dentista</h4></th>
            <th scope="col"><h4>Servicio</h4></th>
            <th scope="col"><h4>Valor</h4></th>
            <th scope="col"><h4>Accion</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="boleta in boletas" :key="boleta">
            <td>
              {{ formDate(boleta.fin_hora) }}
              {{ formHora(boleta.inicio_hora) }}-{{ formHora(boleta.fin_hora) }}
            </td>
            <td>{{ boleta.nombre_ape }}</td>
            <td>{{ boleta.nombre_servicio }}</td>
            <td>{{ boleta.precio }}</td>
            <td>
              <button class="btn btn-primary" v-b-modal.modal-1 @click="boletaImp = boleta">
                Detalle
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <button class="btn btn-success" @click="downloadPdf()">
      Imprimir lista completa
    </button>

<!--boleta-->
  <b-modal size="xl" id="modal-1" class="modal" title="Detalle" hide-footer>
    
    <div class="row flex-d justify-content-around">
      <table id="table" class="table col-10">
        <thead>
          <tr>
            <th scope="col" colspan="2"><h4>Boleta Linda Sonrisa</h4></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              Nombre de profesional
            </td>
            <td>{{ boletaImp.nombre_ape }}</td>
          </tr>
          <tr>
            <td>
              Fecha
            </td>
            <td>
              {{ formDate(boletaImp.fin_hora) }}
            </td>
          </tr>
          <tr>
            <td>
              Hora
            </td>
            <td>
              {{ formHora(boletaImp.inicio_hora) }}-{{ formHora(boletaImp.fin_hora) }}
            </td>
          </tr>

          <tr>
            <td>
              Servicio
            </td>
            <td>{{ boletaImp.nombre_servicio }}</td>
          </tr>
          <tr>
            <td>
              Descripcion
            </td>
            <td>{{ boletaImp.descripcion_servicio }}</td>
          </tr>
          <tr>
            <td>
              Precio
            </td>
            <td>${{ boletaImp.precio }}</td>
          </tr>

        </tbody>
      </table>
    </div>
    <br>
    <button class="btn btn-secondary" @click="$bvModal.hide('modal-1')">Cerrar</button>
    <button class="btn btn-success"   @click="downloadPdf(), $bvModal.hide('modal-1')" style="margin-left:5px;">Imprimir</button>  
    
    <br>
  </b-modal>
    
  </div>
</template>

<script>
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
export default {
  data() {
    return {
      search: "",
      boletas: [],
      boletaImp: {},
    };
  },
  mounted() {
    this.getBoletas();
  },
  methods: {
    formHora(hr) {
      let hora = new Date(hr);
      return `${this.addZero(hora.getHours(), 2)}:${this.addZero(
        hora.getMinutes(),
        2
      )}`;
    },
    formDate(dt) {
      let date = new Date(dt);
      return `${this.addZero(date.getDate(), 2)}/${this.addZero(
        date.getMonth() + 1,
        2
      )}/${date.getFullYear()}`;
    },
    upperCase(str) {
      const stringUpper = str.charAt(0).toUpperCase() + str.slice(1);
      return stringUpper;
    },
    getBoletas() {
      let page = 1;
      this.$axios
        .get(
          "http://127.0.0.1:8000/api/boletas?page=" +
            page +
            "&id_user=" +
            this.$store.getters.value.id_user
        )
        .then((response) => {
          this.boletas = response.data.data;
          console.log(this.boletas);
        });
    },
    addZero(a, b) {
      if (a.toString().length < b) {
        return `0${a}`;
      } else {
        return `${a}`;
      }
    },
    downPdf() {
      var pdf = new jsPDF();
      pdf.text("hello world", 10, 10);
      pdf.save("info.pdf");
    },
    downloadPdf() {
      window.html2canvas = html2canvas;
      var doc = new jsPDF("p", "pt", "a4");

      doc.html(document.querySelector("#table"), {
        callback: function(pdf) {
          pdf.save("");
        },
      });
      //pdf.text('hello world',10,10);
      //pdf.save('info.pdf');
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
