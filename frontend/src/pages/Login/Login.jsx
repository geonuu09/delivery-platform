import React, { useState } from "react";
import "./Login.scss";
import { FaUser, FaLock } from "react-icons/fa";
import kakaoImage from "../../assert/kakao.png";
import naverImage from "../../assert/naver.png";
import { Link, useNavigate } from "react-router-dom";
import { API_BASE_URL as BASE, AUTH } from "../../constants/host";
import axios from "axios";
import { setLoginUserInfo, getLoginUserInfo } from "../../utils/login-util";

function Login() {
    const navigate = useNavigate();
    const API_BASE_URL = BASE + AUTH;
    const [userValue, setUserValue] = useState({
        email: "",
        password: "",
    });

    const loginSubmit = (e) => {
        e.preventDefault();
        console.log(userValue.email, userValue.password);
        axios
            .post(`${API_BASE_URL}/signin`, userValue,)
            .then((res) => {
                console.log("응답:", res); // 응답 전체 확인
                console.log("헤더:", res.headers['authorization']);
                console.log("사용자 이름:", res.data.username);

                setLoginUserInfo(res.data);
                navigate("/");
            })
            .catch((error) => {
                if (error.response) {
                    // 서버에서 응답이 왔을 때 (status code 4xx, 5xx)
                    console.error("서버 응답 에러:", error.response);
                    alert("🤔 이메일 또는 비밀번호를 확인해주세요");
                } else if (error.request) {
                    // 요청이 서버에 도달했으나 응답이 없을 때
                    console.error("서버 응답 없음:", error.request);
                    alert("서버 응답이 없습니다. 나중에 다시 시도해주세요.");
                } else {
                    // 그 외의 에러
                    console.error("로그인 중 에러 발생:", error.message);
                    alert("로그인 중 오류가 발생했습니다.");
                }
            });
    };

    const setEmail = (e) => {
        setUserValue({
            ...userValue,
            email: e.target.value,
        });
    };
    const setPassword = (e) => {
        setUserValue({
            ...userValue,
            password: e.target.value,
        });
    };
    return (
        <div className="container">
            <div className="wrapper">
                <form action="">
                    <h1>
                        <Link to="/" className="home">
                            Delivery
                        </Link>
                    </h1>
                    <h2>Login</h2>
                    <div className="input-box">
                        <input
                            type="email"
                            placeholder="Email"
                            required
                            onChange={setEmail}
                        />
                        <FaUser className="icon" />
                    </div>
                    <div className="input-box">
                        <input
                            type="password"
                            placeholder="Password"
                            required
                            onChange={setPassword}
                        />
                        <FaLock className="icon" />
                    </div>
                    <div className="remember-forget">
                        <Link to="#" className="find">
                            아이디 찾기
                        </Link>
                        <Link to="#" className="find">
                            비밀번호 찾기
                        </Link>
                    </div>
                    <button type="submit" onClick={loginSubmit}>
                        로그인
                    </button>
                    <div className="register-link">
                        <Link to="/register" className="register">
                            회원가입
                        </Link>
                    </div>
                    <div className="social-kakao-login">
                        <img
                            src={naverImage}
                            alt="Naver logo"
                            className="naver"
                        />
                        <img
                            src={kakaoImage}
                            alt="Kakao logo"
                            className="kakao"
                        />
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Login;
