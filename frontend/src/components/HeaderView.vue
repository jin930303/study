<template>
  <header class="main-header">
    <nav class="nav-container">
      <div class="nav-links">
        <router-link to="/">Home</router-link>

        <template v-if="authStore">
          <template v-if="!authStore.isLoggedIn">
            <router-link to="/login">로그인</router-link>
            <router-link to="/member/join">회원가입</router-link>
          </template>

          <template v-else>
            <span class="user-info">접속중</span>
            <button @click="handleLogout" class="logout-btn">로그아웃</button>
          </template>
        </template>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { useAuthStore } from "../stores/auth";

const router = useRouter();

let authStore: any;

try {
  authStore = useAuthStore();
} catch (e) {
  console.error("Pinia가 아직 로드되지 않았습니다.");
}

const handleLogout = () => {
  if (confirm("로그아웃 하시겠습니까?")) {
    authStore.logout();

    // 2. 알림 후 페이지 새로고침 (상태 초기화)
    alert("로그아웃 되었습니다.");
    router.push("/");
  }
};
</script>

<style scoped>
.main-header {
  background-color: #f8f9fa;
  padding: 1rem 2rem;
  border-bottom: 1px solid #ddd;
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-links a {
  text-decoration: none;
  color: #333;
}

.logout-btn {
  padding: 5px 10px;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #ff7875;
}

.user-info {
  font-size: 0.9rem;
  color: #666;
}
</style>
