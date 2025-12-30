import { defineStore } from "pinia";
import { ref } from "vue";
import api from "../api";
import { jwtDecode } from "jwt-decode";

export const useAuthStore = defineStore('auth',() =>{
    //상태 : 토큰 존재 여부로 로그인 초기값 설정

    const isLoggedIn = ref(!!localStorage.getItem("accessToken"));

    //로그인 액션
    const login = (accessToken : string, refreshToken : string ) => {
        localStorage.setItem("accessToken",accessToken);
        localStorage.setItem("refreshToken",refreshToken);
        isLoggedIn.value=true;
    };

    //로그아웃 액션
    const logout =async () => {
        try{
            const token = localStorage.getItem("accessToken");
            if(token){
                const decoded : any = jwtDecode(token);

                const loginId = decoded.sub;
                if(loginId){
                    await api.post("/logout",{loginId});
                    console.log(`${loginId} 서버 로그아웃 완료`);
                }
            }
        }
        catch(error){
            console.error("로그아웃 과정 중 오류 발생",error);
        }
        finally{
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            isLoggedIn.value=false;
            delete api.defaults.headers.common["Authorization"];
        }
        
    };
    return {isLoggedIn,login,logout}
});