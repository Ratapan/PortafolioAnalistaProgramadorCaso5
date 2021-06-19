<template>
  <div class="container">
    <br>
    <div class="row flex-d justify-content-around">
      <h1>Ordenes</h1>
    </div>
    <br />
    <div class="row flex-d justify-content-around">
      <h3><strong> Crear orden </strong></h3>
    </div>
    <br />
    <!--
    <div class="row flex-d justify-content-around">
      <button type="button" class="btn btn-primary" @click="sta()">Agregar</button>
    </div>
    <br />
-->
    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Tipo productos</label>
      </div>
      <div class="col-6">
        <select v-model="family" class="form-control form-control-sm">
          <option v-for="famil in familys" :key="famil.id_t_fam" :value="famil.id_t_fam">{{ famil.nombre_tipo_familia }}</option>
        </select>
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Productos</label>
      </div>
      <div class="col-6">
        <select v-model="product" class="form-control form-control-sm">
          <option v-for="produc in products" :key="produc.id_tipop" :value="produc.id_tipop">{{ produc.nombre_tipop }}</option>
        </select>
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center" v-if="product != ''">
      <div class="col-4">
        <label>Descripciones</label>
      </div>
      <div class="col-6">
        <p>{{product.desc_tipop}}</p>
      </div>
      <br>
    </div>
    <br>

    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>N de productos</label>
      </div>
      <div class="col-6">
        <input v-model="cantidad" class="form-control form-control-sm" type="text">
      </div>
      <br>
    </div>
    <br>
    <div class="row flex-d justify-content-center">
      <div class="col-4">
        <label>Proveedor</label>
      </div>
      <div class="col-6">
        <select v-model="prov" class="form-control form-control-sm">
          <option v-for="prove in provs" :key="prove.id_proveedor" :value="prove.id_proveedor">{{ prove.nombre_ape }}</option>
        </select>
      </div>
      <br>
    </div>
    
    <br>
    <div class="row flex-d justify-content-center">
      <button type="button" class="btn btn-success" @click="storeOrden()">Realizar pedido</button>
    </div>
    <br />
    <br />
    <br />
    <div class="row flex-d justify-content-around">
      <table class="table">
        <thead>
          <tr>
            <th scope="col"><h3> Nombre </h3></th>
            <th scope="col"><h3>Stock</h3></th>
            <th scope="col"><h3>Stock critico</h3></th>
            <th scope="col"><h3>Acciones</h3></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="Product in allProducts" :key="Product.id_tipop" :class="statusColor(Product.stock_tipop,Product.stock_c_tipop)">
            <td>{{Product.nombre_tipop}}</td>
            <td>{{Product.stock_tipop}}</td>
            <td>{{Product.stock_c_tipop}}</td>
            <td>  
              <div class="row flex-d justify-content-around">
                <button type="button" class="btn btn-susses">Ver</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
      <br>
      <br>
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
      family :'',
      prov :'',
      product:'',

      familys :{},
      provs :{},
      products:{},
      allProducts:{},
    };
  },
  watch: {
    family(){
      this.product = {};
      this.getProduct();
      this.getProveedor();
    }
  },
  mounted() {
    this.getFamily();
    this.getProducts();
    this.getProveedor();
  },
  methods: {
    sta(){
      if(this.stat == false){this.stat = true}else{this.stat = false}
    },
    logout() {
      this.$store.getters.value = null; 
    },
    getFamily(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/familia?page=" + page).
            then(response => {
              this.familys = response.data.data;
              //console.log(this.familys)
      });
    },
    getProveedor(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/proveedores?page=" + page).
            then(response => {
              this.provs = response.data.data;
              //console.log(this.provs)
      });
    },
    getProduct(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/producto?page=" + page + "&id_familia=" + this.family).
            then(response => {
              this.products = response.data.data;
              //console.log(this.products)
      });
    },
    getProducts(){
            let page = 1
            this.$axios.
            get("http://127.0.0.1:8000/api/productos?page=" + page).
            then(response => {
              this.allProducts = response.data.data;
              //console.log(this.allProducts)
      });
    },
    storeOrden() {
      this.$axios
        .post("http://127.0.0.1:8000/api/ordenes", {
          id_proveedor:    this.prov,
          id_user:     this.$store.getters.value.id_user,
          cantidad:     this.cantidad,
          id_producto:  this.product,
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
    statusColor(st,stc){
      //console.log(st,'>=',stc)
      if(parseInt(st) == parseInt(stc)){
        return "table-warning"
      }
      if(parseInt(st) > parseInt(stc)){
        return "table-success"
      }
      else{
        return "table-danger"
      }
    },

  }
};
</script>

<style scoped>
.form-control {
    display: block;
    width: 210px;
  }
</style>
