<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Ordenes</h1>
    </div>
    <br />
    <div>
      <h1>hohoho</h1>
    </div>
    <br />
  </div>
</template>

<script>
export default {
  data() {
    return{
      stat: false,
      dayHour:'',
      iniHour:'',
      endHour:'',
    };
  },
  methods: {
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },


  }
};
</script>

<style></style>
