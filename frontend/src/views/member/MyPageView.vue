<template>
  <div class="my-page-container">
    <h2>내 정보 보기</h2>
    <div v-if="userInfo" class="info-box">
      <div v-if="!isEditMode">
        <div class="info-row">
          <strong>아이디 :</strong>{{ userInfo.loginId }}
        </div>
        <div class="info-row">
          <strong>닉네임 :</strong>{{ userInfo.nickname }}
        </div>
        <div class="info-row">
          <strong>이메일 :</strong>{{ userInfo.email }}
        </div>
        <div class="info-row">
          <strong>핸드폰 :</strong>{{ userInfo.phone }}
        </div>
        <div class="info-row">
          <strong>가입일 :</strong>{{ userInfo.createAt }}
        </div>
        <div class="btn-group">
          <button @click="toggleEditMode" class="edit-btn">수정하기</button>
        </div>
      </div>

      <div v-else>
        <div class="form-group">
          <label>아이디</label>
          <input
            type="text"
            v-model="editForm.loginId"
            disabled
            class="disabled-input"
          />
        </div>
        <div class="form-group">
          <label>닉네임</label>
          <input
            type="text"
            v-model="editForm.nickname"
            placeholder="닉네임을 입력하세요"
          />
        </div>
        <div class="form-group">
          <label>이메일</label>
          <input
            type="email"
            v-model="editForm.email"
            placeholder="이메일을 입력하세요"
          />
        </div>
        <div class="form-group">
          <label>핸드폰</label>
          <input
            type="tel"
            v-model="editForm.phone"
            placeholder="000-0000-0000"
            @input="formatPhoneNumber"
          />
        </div>
        <div class="btn-group">
          <button @click="handleUpdate" class="save-btn">저장</button>
          <button @click="toggleEditMode" class="cancel-btn">취소</button>
        </div>
      </div>
    </div>
    <div v-else class="loading">정보를 불러오는 중...</div>
  </div>
</template>

<script setup lang="ts">
import api from "../../api";
import { ref, onMounted, reactive } from "vue";

//상태관리
const userInfo = ref<any>(null);
const isEditMode = ref(false);
const editForm = reactive({
  loginId: "",
  nickname: "",
  email: "",
  phone: "",
});

const fetchMyInfo = async () => {
  try {
    const response = await api.get("/member/me");
    userInfo.value = response.data;
    editForm.nickname = response.data.nickname;
    editForm.email = response.data.email;
    editForm.phone = response.data.phone;
  } catch (error) {
    console.error("정보를 가져오는데 실패했습니다.", error);
    alert("로그인 세션이 만료되었습니다.");
  }
};

const formatPhoneNumber = (e: any) => {
  const value = e.target.value.replace(/\D/g, "");
  if (value.length <= 11) {
    editForm.phone = value.replace(/(\d{3})(\d{3,4})(\d{4})/, "$1-$2-$3");
  }
};

const toggleEditMode = () => {
  if (isEditMode.value) {
    //취소 시 데이터를 원래대로 복구
    editForm.nickname = userInfo.value.nickname;
    editForm.email = userInfo.value.email;
    editForm.phone = userInfo.value.phone;
  }
  isEditMode.value = !isEditMode.value;
};

const handleUpdate = async () => {
  try {
    await api.put("/member/me", editForm);
    alert("수정되었습니다.");
    await fetchMyInfo();
    isEditMode.value = false;
  } catch (error) {
    alert("수정 실패");
  }
};

const formatDate = (dateString: string) => {
  if (!dateString) return "";
  return new Date(dateString).toLocaleDateString();
};

onMounted(() => {
  fetchMyInfo();
});
</script>
<style scoped>
.info-container {
  max-width: 500px;
  margin: 3rem auto;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
.info-row {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px f9f9f9 solid;
  display: flex;
  justify-content: space-between;
}
.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}
.form-group label {
  font-weight: bold;
  margin-bottom: 5px;
}
.form-group input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.disabled-input {
  background: #f0f0f0;
}
.btn-group {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.edit-btn {
  width: 100%;
  padding: 10px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.save-btn {
  flex: 1;
  padding: 10px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.cancel-btn {
  flex: 1;
  padding: 10px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.loading {
  text-align: center;
  color: #888;
}
</style>
