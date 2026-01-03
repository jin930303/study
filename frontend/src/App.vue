<template>
  <div id="app">
    <header class="app-header">
      <HeaderView />
    </header>

    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import HeaderView from "./components/HeaderView.vue";
</script>

<style scoped>
html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-header {
  position: fixed; /* 상단 고정 */
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000; /* 본문보다 위에 오도록 설정 */
  background-color: white; /* 투명 방지 */
  border-bottom: 1px solid #eee;
}

.content {
  margin-top: 60px; /* 헤더 높이만큼 여백을 주어 내용이 가려지지 않게 함 */
  padding: 20px;
  flex: 1; /* 남은 공간을 본문이 다 차지하도록 설정 */
}
</style>
