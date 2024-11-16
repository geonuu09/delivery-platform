import React from "react";
import "./MyInfoPage.scss";
import { useNavigate } from "react-router-dom";

const MyInfoPage = () => {
    const navigate = useNavigate();
    const handleLogout = () => {
        // 로컬 스토리지에서 토큰을 제거
        // localStorage.removeItem("ACCESS_TOKEN");
        // localStorage.removeItem("LOGIN_USEREMAIL");
        // localStorage.removeItem("USER_GRADE");
        // localStorage.removeItem("USER_ID"); 
        // localStorage.removeItem("USER_PHONE");
        // localStorage.removeItem("LOGIN_USERNAME");
        // localStorage.removeItem("TanstackQueryDevtools.open");
        navigate("/login"); 
    };

    return (
        <div className="my-info-page-container">
            <div className="my-info-page-wrap">
                <h1>내 정보 수정</h1>
                <img
                    className="my-info-page-profile-img"
                    src={require("../../../assert/user-icon.png")}
                ></img>
                <div>
                    <div className="my-info-page-name">닉네임</div>
                    <input type="text" placeholder="먹자" />
                </div>
                <div>
                    <div className="my-info-page-email">
                        대표 이메일
                        <input
                            type="text"
                            placeholder="youngsik823@naver.com"
                        />
                    </div>
                </div>

                <div>
                    <div className="my-info-page-phone">
                        휴대폰 번호 변경
                        <input type="text" placeholder="010-1234-5678" />
                    </div>
                </div>
                <button className="my-info-page-btn">수정하기</button>
                <div className="my-info-page-box-btn">
                    <div className="my-info-page-logout" onClick={handleLogout}>로그아웃</div>
                    <div className="my-info-page-delete">회원탈퇴</div>
                </div>
            </div>
        </div>
    );
};

export default MyInfoPage;
