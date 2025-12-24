<template>
  <div class="join-container">
    <h2>회원가입</h2>
    <form @submit.prevent="handleJoin">
      <div class="form-group">
        <label>아이디</label>
        <div class="input-with-btn">
          <input
            v-model="form.loginId"
            type="text"
            placeholder="아이디를 입력하세요"
            required
          />
          <button type="button" @click="checkDuplicate('id')">중복확인</button>
        </div>
        <span v-if="checks.id" class="success-msg"
          >사용 가능한 아이디입니다.</span
        >
      </div>

      <div class="form-group">
        <label>비밀번호</label>
        <input
          v-model="form.password"
          type="password"
          placeholder="8자 이상, 대문자, 특수문자 포함"
          required
        />
        <div>
          <span v-if="form.password && !passwordValid" class="error-msg">
            8자 이상, 대문자 및 특수문자를 최소 1개 이상 포함해야 합니다.
          </span>
        </div>
      </div>
      <div class="form-group">
        <label>비밀번호 확인</label>
        <input
          v-model="passwordConfirm"
          type="password"
          placeholder="비밀번호 확인"
          required
        />
        <div>
          <span v-if="passwordConfirm && !isPasswordMatch" class="error-msg"
            >비밀번호가 일치하지 않습니다.</span
          >
          <span v-if="passwordConfirm && isPasswordMatch" class="success-msg"
            >비밀번호가 일치합니다.</span
          >
        </div>
      </div>
      <div class="form-group">
        <label>닉네임</label>
        <div class="input-with-btn">
          <input
            v-model="form.nickname"
            type="text"
            placeholder="닉네임을 입력하세요"
            required
          />
          <button type="button" @click="checkDuplicate('nickname')">
            중복확인
          </button>
        </div>
        <div v-if="checks.nickname" class="success-msg">
          사용 가능한 닉네임입니다.
        </div>
      </div>

      <div class="form-group">
        <label>이메일</label>
        <div class="input-with-btn">
          <input
            v-model="form.email"
            type="email"
            placeholder="example@email.com"
            required
          />
          <button type="button" @click="checkDuplicate('email')">
            중복확인
          </button>
        </div>
        <span v-if="checks.email" class="success-msg"
          >사용가능한 이메일입니다.</span
        >
      </div>
      <div class="form-group">
        <label>휴대폰 번호</label>
        <div class="input-with-btn">
          <input
            v-model="form.phone"
            type="tel"
            placeholder="010-0000-0000"
            @input="formatPhoneNumber"
          />
          <button type="button" @click="checkDuplicate('phone')">
            중복확인
          </button>
        </div>
        <span v-if="checks.phone" class="success-msg"
          >사용 가능한 번호입니다.</span
        >
      </div>

      <button type="submit" class="join-btn" :disabled="!isFormValid">
        가입하기
      </button>
    </form>
  </div>
</template>
<script setup lang="ts">
import { computed, reactive, ref } from "vue";
import api from "../api";
import { useRouter } from "vue-router";

const router = useRouter();
const passwordConfirm = ref("");
const passwordValid = computed(() => {
  const regex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
  return regex.test(form.password);
});
const checks = reactive({
  id: false,
  nickname: false,
  email: false,
  phone: false,
});
const form = reactive({
  loginId: "",
  password: "",
  nickname: "",
  email: "",
  phone: "",
});

const isPasswordMatch = computed(() => form.password === passwordConfirm.value);

const isFormValid = computed(() => {
  return (
    isPasswordMatch.value &&
    checks.id &&
    checks.nickname &&
    checks.email &&
    checks.phone
  );
});

const formatPhoneNumber = (e: any) => {
  const value = e.target.value.replace(/\D/g, "");
  if (value.length <= 11) {
    form.phone = value.replace(/(\d{3})(\d{3,4})(\d{4})/, "$1-$2-$3");
  }
};

const checkDuplicate = async (type: string) => {
  let url = "";
  let val = "";

  if (type == "id") {
    url = "/member/check-id?loginId=";
    val = form.loginId;
  } else if (type == "nickname") {
    url = "/member/check-nickname?nickname=";
    val = form.nickname;
  } else if (type == "email") {
    url = "/member/check-email?email=";
    val = form.email;
  } else if (type == "phone") {
    url = "member/check-phone?phone=";
    val = form.phone;
  }
  if (!val) return alert("값을 입력해주세요");
  try {
    const response = await api.get(url + val);
    if (response.data) {
      alert("이미 사용 중입니다.");
      (checks as any)[type] = false;
    } else {
      alert("사용 가능합니다.");
      (checks as any)[type] = true;
    }
  } catch (error) {
    alert("오류가 발생했습니다.");
  }
};

const handleJoin = async () => {
  if (!isFormValid.value) return alert("다시 확인해주세요");
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
.input-with-btn {
  display: flex;
  gap: 10px;
}
.input-with-btn input {
  flex: 1;
}
.input-with-btn button {
  padding: 0 10px;
  cursor: pointer;
  background: #eee;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.error-msg {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}
.success-msg {
  color: green;
  font-size: 12px;
  margin-top: 5px;
}
.join-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
