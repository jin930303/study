<template>
  <div class="login-container">
    <h2>로그인</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-gorup">
        <label>아이디</label>
        <input type="text" v-model="loginForm.loginId" required />
      </div>
      <div class="form-gorup">
        <label>비밀번호</label>
        <input v-model="loginForm.password" type="password" required />
      </div>
      <button type="submit" class="login-btn">로그인</button>
    </form>
    <p @click="router.push('/member/join')" class="go-join">
      계정이 없으신가요? 회원가입
    </p>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { useRouter } from "vue-router";
import api from "../../api";
import { useAuthStore } from "../../stores/auth";

const authStore = useAuthStore();

const router = useRouter();
const loginForm = reactive({
  loginId: "",
  password: "",
});

const handleLogin = async () => {
  console.log("로그인 시도 시작...");
  try {
    const response = await api.post("/login", loginForm);
    console.log("전체 응답 객체 :", response);
    const { accessToken, refreshToken } = response.data;

    //1.브라우저 로컬 스토리지에 저장
    if (accessToken && refreshToken) {
      authStore.login(accessToken, refreshToken);

      //2. 이후 나가는 모든 axios 요청 헤더에 토큰 설정
      api.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;

      alert("로그인 성공");
      router.push("/");
    }
  } catch (error: any) {
    const errorMsg =
      error.response?.data || "아이디 또는 비밀번호를 확인하세요.";

    // 만약 백엔드에서 에러 메시지를 String으로 보낸다면 바로 출력,
    // 객체로 보낸다면 특정 필드(예: errorMsg.message)를 출력해야 합니다.
    alert(
      typeof errorMsg === "string"
        ? errorMsg
        : "로그인 정보가 올바르지 않습니다."
    );

    console.error("로그인 실패 상세:", error.response);
  }
};

const handleLogout = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");

  delete api.defaults.headers.common["Authorization"];
  alert("로그아웃 되었습니다.");
  window.location.href = "/";
};
</script>
<style scoped>
.login-container {
  max-width: 350px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}
.login-btn {
  width: 100%;
  padding: 10px;
  background: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.go-join {
  margin-top: 15px;
  text-align: center;
  color: #666;
  cursor: pointer;
  font-size: 0.9rem;
}
</style>
