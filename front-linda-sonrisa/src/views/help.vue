<template>
<div>
  <div v-for="inf in info" :key="inf">
    <h5 v-if="inf.title">{{inf.title}}</h5>
    <p  v-if="inf.parr" style="max-width:600px; padding-left:10px;">{{inf.parr}}</p>
  </div>
</div>
</template>

<script>
export default {
props:{
  info:{
    type: Object
  },
},
}
</script>

<style>

</style>