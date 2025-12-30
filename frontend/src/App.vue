<template>
  <div id="app">
    <HeaderView />

    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import HeaderView from "./components/HeaderView.vue";
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.content {
  padding: 20px;
}
</style>
