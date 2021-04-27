<template>
  <section class="container">
    <div class="row">
      <div class="col-12 d-flex justify-content-center">
        <div class="d-flex text-center">
          <h2 class="title graph"> Clinica odontologica Linda Sonrisa</h2>
        </div>
      </div>
    </div>
    <br>
    <div class="row d-flex justify-content-center">
      <div class="col-xl-8 col-md-10 col-sm-12">
        <img class="img-presentation" src="../../assets/stock_dentista.jpg" alt="">
      </div>
    </div>
    <br>
    <br>
    <div class="row d-flex justify-content-around">
      <div class="col-lg-3 col-md-4 col-sm-6 d-flex justify-content-center" v-for="se in services" :key="se">
        <div>
          <div class="card cardfrm">
            <img class="card-img-top" src="" alt="Card image cap">
            <div class="card-body">
              <h3>{{se.nombre_servicio}}</h3>
              <p class="card-text">{{se.descripcion_servicio}}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <br>
    <br>
  </section>
</template>

<script>
  import { gsap } from 'gsap';
  import { CSSPlugin } from 'gsap/CSSPlugin'
  gsap.registerPlugin(CSSPlugin);

  export default {
    data() {
      return {
        services:{},
      };
    },
    mounted(){
      this.titleStile();
      this.getService();
    },
    methods: {
      titleStile(){
        //gsap.to(graph,{ duration: 2.5, ease: "sine.out", y: -500});
      },
      getService(){
        let page = 1
        this.$axios.
        get("http://127.0.0.1:8000/api/servicio?page=" + page).
        then(response => {
          this.services = response.data.data;
          console.log(this.services)
        });
      }
    },
  }
</script>
<style scoped>
.img-presentation{
  border: 3px solid rgba(0, 0, 0, 0.5);
  border-radius: 35px;
  height: 500px;
}
.cardfrm{
  height: 200px;
  margin: 10px;
}
</style>