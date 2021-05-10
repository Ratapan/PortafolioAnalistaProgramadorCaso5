<template>
  <div class="container">
    <ul class="list-group">
      <li class="list"  v-for="hora in horas" :key="hora"><button class="btn btn-info" @click="storeHora(hora.id_hora)">{{btnHora(hora.inicio_hora)}} | {{btnHora(hora.fin_hora)}}</button></li>
    </ul>
    
  </div>
</template>

<script>
export default {
  props: {
    empleado: {
      type: Object
    }
  },
  data() {
    return{
      stat: false,
      openI: false,
      openE: false,
      dayHour:'',
      iniHour:'',
      endHour:'',
      horas:[],
    };
    
  },
  mounted() {
  },
  watch: {
    empleado: function() {
      this.getHoras();
    }
  },
  methods: {
    btnHora(hr){
      let hora = new Date(hr)
      return `${this.addZero(hora.getHours(),2)}:${this.addZero(hora.getMinutes(),2)}`
    },
    addZero(a,b){
      if(a.toString().length < b){return `0${a}` }else{return `${a}`}
    },
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },
    handleChange(value, type) {
      if (type === 'second') {
        this.open = false;
      }
    },
    storeHora(id) {
      console.log(id)
      this.$axios
        .post("http://127.0.0.1:8000/api/cita", {
          id_hora:         id,
          id_user:         this.$store.getters.value.id_user,
        })
        .then((response) => {
          console.log(response);
          this.getHoras();
        })
        .catch((err) => {
          this.error = err;
        });
    },
    getHoras(){
      let page = 1
      this.$axios.
      get("http://127.0.0.1:8000/api/hora-d?page="+page+"&id_emp="+ this.empleado.id_empleado).
      then(response => {
        this.horas = response.data.data;
        console.log(this.horas)
      });
    },
  }
};
</script>

<style scoped>
.list {
  list-style:none;
  margin: 5px 0px;
}
</style>