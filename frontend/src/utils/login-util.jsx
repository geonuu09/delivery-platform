export const setLoginUserInfo = ({ role, userId, email }) => {
    // 토큰 객체에 더 추가하면 더 넣을수 있음
    localStorage.setItem("LOGIN_USEREMAIL", email);
    localStorage.setItem("USER_GRADE", role);
    localStorage.setItem("USER_ID", userId);
};

// 로그인한 유저의 데이터객체를 반환하는 함수
export const getLoginUserInfo = () => {
    return {
        userEmail: localStorage.getItem("LOGIN_USEREMAIL"),
        userGrade: localStorage.getItem("USER_GRADE"),
        userId: localStorage.getItem("USER_ID"),
    };
};

// 로그인 id 들고다닐 함수
export const getUserInfo = () => {
    return {
        userId: localStorage.getItem("USER_ID"),
        userEmail: localStorage.getItem("LOGIN_USEREMAIL"),
        userGrade: localStorage.getItem("USER_GRADE"),
    };
};

export const isLogin = () => localStorage.getItem("ACCESS_TOKEN");
