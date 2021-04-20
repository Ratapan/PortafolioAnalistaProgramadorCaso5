<template>
  <section class="container">
    <div class="row">
      <div class="col-12 d-flex justify-content-center">
        <div class="card d-flex text-center">
          <h2 class="title graph"> Clinica odontologica Linda Sonrisa</h2>
        </div>
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