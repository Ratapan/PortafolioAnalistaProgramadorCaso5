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
  </section>
</template>

<script>
  import { gsap } from 'gsap';
  import { CSSPlugin } from 'gsap/CSSPlugin'
  gsap.registerPlugin(CSSPlugin);

  export default {
    data() {
      return {
      };
    },
    mounted(){
      this.titleStile()
    },
    methods: {
      titleStile(){
        //gsap.to(graph,{ duration: 2.5, ease: "sine.out", y: -500});
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
</style>