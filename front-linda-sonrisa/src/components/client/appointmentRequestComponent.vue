<template>
  <div class="container">
    <div class="row flex-d justify-content-around">
      <h1>Pedir citas (usuario logeado)</h1>
    </div><br>
    <div class="row flex-d justify-content-around">
      <div class="col-6">
        <input type="text" class="form-control form-control-sm" v-model="search">
      </div>
    </div>
  </div>
</template>

<script>
export default {
data() {
  return {
    search: '',
  }
},
}
</script>

<style>

</style>