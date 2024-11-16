import React from "react";
import { Navigate, Outlet } from "react-router-dom";

const PrivateRoute = () => {
    // const token = localStorage.getItem("ACCESS_TOKEN");

    // if (!token) {
    //     // 토큰이 없으면 로그인 페이지로 리디렉션
    //     return <Navigate to="/login" />;
    // }

    return <Outlet />; // 토큰이 있으면 자식 컴포넌트 렌더링
};

export default PrivateRoute;
