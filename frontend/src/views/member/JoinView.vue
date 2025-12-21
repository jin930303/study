<template>
  <div class="join-container">
    <h2>회원가입</h2>
    <form @submit.prevent="handleJoin">
      <div class="form-group">
        <label>아이디</label>
        <input
          v-model="form.loginId"
          type="text"
          placeholder="아이디를 입력하세요"
          required
        />
      </div>
      <div class="form-group">
        <label>비밀번호</label>
        <input
          v-model="form.password"
          type="password"
          placeholder="비밀번호를 입력허세요"
          required
        />
      </div>
      <div class="form-group">
        <label>닉네임</label>
        <input
          v-model="form.nickname"
          type="text"
          placeholder="닉네임을 입력하세요"
          required
        />
      </div>
      <div class="form-group">
        <label>이메일</label>
        <input
          v-model="form.email"
          type="email"
          placeholder="example@email.com"
          required
        />
      </div>
      <div class="form-group">
        <label>휴대폰 번호</label>
        <input
          v-model="form.phone"
          type="tel"
          placeholder="010-0000-0000"
          @input="formatPhoneNumber"
        />
      </div>
      <button type="submit" class="join-btn">가입하기</button>
    </form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import api from "../../api";
import { useRouter } from "vue-router";

const router = useRouter();

const form = reactive({
  loginId: "",
  password: "",
  nickname: "",
  email: "",
  phone: "",
});

const formatPhoneNumber = (e: any) => {
  const value = e.target.value.replace(/\D/g, "");
  if (value.length <= 11) {
    form.phone = value.replace(/(\d{3})(\d{3,4})(\d{4})/, "$1-$2-$3");
  }
};

const handleJoin = async () => {
  try {
    const response = await api.post("/member/join", form);
    alert(`${response.data.nickname}님, 환영합니다. 가입이 완료되었습니다.`);
    router.push("/login");
  } catch (error: any) {
    console.error(error);
    alert(error.response?.data?.message || "회원가입에 실패했습니다.");
  }
};
</script>
<style scoped>
.join-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}
.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}
.form-group input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.join-btn {
  width: 100%;
  padding: 12px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.join-btn:hover {
  background-color: #45a049;
}
</style>
