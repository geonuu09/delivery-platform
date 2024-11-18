import React, { useState } from "react";
import "./MyInfo.scss";
import MyInfoPage from "../../components/MyInfo/MyInfoPage/MyInfoPage";

const Myinfo = () => {
    const [myPageBtn, setMyPageBtn] = useState("mypage");
    return (
        <div className="my-Info-container">
            <div className="my-Info-wrap">
                <h1>내정보</h1>
                <div className="my-Info-box">
                    <img
                        className="my-Info-profile"
                        src={require("../../assert/user-icon.png")}
                    ></img>
                    <div className="my-Info-box-wrap">
                        <p onClick={() => setMyPageBtn("mypage")}>냠냠</p>
                        <div className="my-Info-btn-box">
                            <div
                                className="my-Info-btn"
                                onClick={() => setMyPageBtn("review")}
                            >
                                리뷰관리
                            </div>
                            <div onClick={() => setMyPageBtn("adress")}>
                                주소관리
                            </div>
                        </div>
                    </div>
                </div>
                {myPageBtn === "mypage" && <MyInfoPage />}
                {myPageBtn === "review" && (
                    <div>리뷰 관리</div>
                )}
                {myPageBtn === "adress" && (
                    <div>주소 관리</div>
                )}
            </div>
        </div>
    );
};

export default Myinfo;
