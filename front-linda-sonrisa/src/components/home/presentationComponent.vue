<template>
  <div class="container">
    <br />
    <br />

    <div class="row">
      <div class="col-12 d-flex justify-content-center">
        <div class="d-flex text-center">
          <h2 class="title graph">Clínica odontológica Linda Sonrisa</h2>
        </div>
      </div>
    </div>
    <br />
    <br />

    <div class="row d-flex justify-content-center">
      <div class="col-xl-8 col-md-10 col-sm-12 d-flex justify-content-center">
        <img
          class="img-presentation"
          height="600"
          src="../../assets/stock_dentista.jpg"
          alt=""
        />
      </div>
    </div>
    <br />
    <br />
    <div class="row d-flex justify-content-center">
      <div class="col-xl-8 col-md-10 col-sm-12 d-flex justify-content-center">
      <!--<iframe class="img-presentation" width="900" height="500" src="https://www.youtube.com/embed/pDDCKGV8cuM?list=TLPQMDkwNjIwMjHI-miw1YwQiA" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" ></iframe>-->
      <iframe class="img-presentation" width="900" height="600" src="https://www.youtube.com/embed/Fdhnurf6PuI?controls=0" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
      </div>
    </div>
    <br />
    <br />
    <div class="row d-flex justify-content-around">
      <div
        class="col-xl-4 col-lg-4 col-md-6 col-sm-12 d-flex justify-content-center"
        v-for="se in services"
        :key="se.id_servicio"
      >
        <div class="card cardfrm">
          <img class="img-serv" :src="getImgService(se.nombre_servicio)" />
          <div class="card-body">
            <h3>{{ upperCase(se.nombre_servicio) }}</h3>
            <p class="card-text">{{ upperCase(se.descripcion_servicio) }}</p>
          </div>
        </div>
      </div>
    </div>
    <br />
    <br />
  </div>
</template>

<script>
import { gsap } from "gsap";
import { CSSPlugin } from "gsap/CSSPlugin";
gsap.registerPlugin(CSSPlugin);

export default {
  data() {
    return {
      services: {},
    };
  },
  mounted() {
    this.titleStile();
    this.getService();
  },
  methods: {
    upperCase(str) {
      const stringUpper = str.charAt(0).toUpperCase() + str.slice(1);
      return stringUpper;
    },
    titleStile() {
      //gsap.to(graph,{ duration: 2.5, ease: "sine.out", y: -500});
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
    getImgService(im) {
      let page = `https://raw.githubusercontent.com/Ratapan/PortafolioAnalistaProgramadorCaso5/main/IMGs/${im}.png`;
      return page;
    },
  },
};
</script>
<style scoped>
.img-presentation {
  border: 3px solid rgba(0, 0, 0, 0.5);
  border-radius: 35px;
}
.img-serv {
  border: 0px solid rgba(0, 0, 0, 0.5);
  border-radius: 35px;
  min-height: 250px;
  min-width: 250px;
}
.cardfrm {
  border: 3px solid rgba(47, 0, 255, 0.225);
  border-radius: 35px;
  padding: 10px;
  min-height: 200px;
  width: 350px;
  margin: 15px 5px;
}
.card-body {
  flex: 1 1 auto;
  min-height: 1px;
  max-width: 500px;
  padding: 1.25rem;
}
.container,
.container-sm,
.container-md,
.container-lg,
.container-xl {
  max-width: 1800px;
}
.row {
  display: flex;
  flex-wrap: wrap;
  margin-right: -15px;
  margin-left: -15px;
}
.title {
  font-size: 50px;
  font-weight: 600;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 10px 30px 20px 30px;
  border-radius: 35px;
}
</style>
