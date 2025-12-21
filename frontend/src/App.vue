

<template>
 <nav>
  <RouterLink to="/">Home</RouterLink> |
  <RouterLink to="/login">login</RouterLink>
 </nav>
 <RouterView/>
</template>
<style scoped>
/* 간단한 스타일 예시 */
nav {
  padding: 30px;
}
nav a {
  font-weight: bold;
  color: #2c3e50;
  margin: 0 10px;
}
nav a.router-link-exact-active {
  color: #42b983; /* 현재 활성화된 메뉴 색상 */
}
</style>
