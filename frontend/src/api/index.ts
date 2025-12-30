import axios from 'axios';

const api = axios.create({
    baseURL : 'http://localhost:8081/api',
    headers : {
        'Content-Type' : 'application/json',
    },
    withCredentials : true,
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    if(token){
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

//[응답 인터셉터] 401 에러(토큰 만료) 발생 시 처리
api.interceptors.response.use(
    (response) => response, // 정상 응답은 그대로 반환
    async (error) =>{
        const originalRequest = error.config;

        //401 에러이고, 재시도한 적이 없는 요청이라면
        if(error.response?.status === 401 && !originalRequest._retry){
            originalRequest._retry = true;

            try{
                const refreshToken = localStorage.getItem("refreshToken");

                //서버에 토큰 재발급 요청 (백엔드에 해당 API가 구현되어 있어야 함)
                const res = await axios.post('http://localhost:8081/api/login/refresh',{
                    refreshToken : refreshToken
                });

                if(res.status === 200){
                    const newAccessToken = res.data.accessToken;

                    //새 토큰 저장
                    localStorage.setItem("accessToken",newAccessToken);

                    //원래 하려던 요청의 헤더를 새 토큰으로 교체 후 재시도
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                    return api(originalRequest);
                }
            }
            catch(refreshError){
                //Refresh Token 도 만료되었다면 로그아웃 처리
                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");
                window.location.href = "/login";
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);


export default api;