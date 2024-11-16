import React, { useState } from "react";
import "./Join.scss";
import { FaUser, FaLock, FaSign, FaAddressBook, FaRegAddressBook } from "react-icons/fa";
import kakaoImage from "../../assert/kakao.png";
import naverImage from "../../assert/naver.png";
import { Link, useNavigate } from "react-router-dom";
import DaumPost from "../../components/Join/DaumPost";
import axios from "axios";
import { API_BASE_URL as BASE, USER } from "../../constants/host";
import { PiUserListFill } from "react-icons/pi";
import { IoIosPhonePortrait } from "react-icons/io";

function Register() {
    const navigate = useNavigate();
    const API_BASE_URL = BASE + USER;
    const [profileImage, setProfileImage] = useState(null); // 프로필 이미지 상태 추가
    const [profileImageBlob, setProfileImageBlob] = useState(null); // 프로필 이미지
    const [userValue, setUserValue] = useState({
        email: "",
        password: "",
        userName: "",
        phoneNum: "",
        streetAddress: "",
        detailAddress: "",
        role: "",
    });

    const [message, setMessage] = useState({
        email: "",
        password: "",
        userName: "",
        phoneNum: "",
        streetAddress: "",
        detailAddress: "",
    });

    const [correct, setCorrect] = useState({
        email: false,
        password: false,
        userName: false,
        phoneNum: false,
        streetAddress: false,
        detailAddress: false,
        role: false,
    });

    const [selectedRole, setSelectedRole] = useState("");

    const registerButtonClickHandler = (e) => {
        e.preventDefault();

        const allCorrect = Object.values(correct).every(
            (value) => value === true
        );

        if (allCorrect) {
            // 모든 필드가 올바르게 입력된 경우 처리 로직
            console.log("모든 필드가 올바르게 입력되었습니다.");

            const userFormData = new FormData();
            const user = JSON.stringify(userValue);
            const blob = new Blob([user], { type: "application/json" });
            userFormData.append("user", blob);
            userFormData.append("profileImage", profileImageBlob);

            console.log(user);

            axios
                .post(API_BASE_URL + "/signup", userFormData, {
                    headers: {
                        "Contest-Type": "multipart/form-data",
                    },
                })
                .then((response) => {
                    console.log("Registration successful:", response.data);
                    navigate("/login");
                })
                .catch((error) => {
                    console.error("Registration failed:", error);
                });
        } else {
            // 하나 이상의 필드가 올바르지 않은 경우 처리 로직
            const user = JSON.stringify(userValue);
            console.log(user);
            console.log("입력이 올바르지 않은 필드가 있습니다.");
            alert("모두 입력하세요.");
        }
    };

    const emailHandler = async (e) => {
        const regExp =
            /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        let msg;
        let flag = false; // 초기값 설정
        const inputVal = e.target.value;

        if (!inputVal) {
            msg = "이메일을 작성해주세요.";
        } else if (!regExp.test(inputVal)) {
            msg = "이메일 형식으로 작성해주세요.";
        } else {
            const checkResult = await emailCheck(inputVal);
            msg = checkResult.msg;
            flag = checkResult.flag;
        }

        saveInputState({
            key: "email",
            inputVal,
            msg,
            flag,
        });
    };
    // 이메일 중복 체크
    const emailCheck = async (inputVal) => {
        let msg = "";
        let flag = false;
        try {
            const res = await axios.get(
                `${API_BASE_URL}/users/email/check`, {
                    params: { email: inputVal },
                }
            );
            if (res.status === 200) {
                if (res.data) {
                    // res.data가 true이면 이메일 중복
                    msg = "이메일이 중복되었습니다.";
                } else {
                    // res.data가 false이면 이메일 사용 가능
                    msg = "사용 가능한 이메일 입니다.";
                    flag = true;
                }
            } else {
                msg = "서버 통신이 원활하지 않습니다.";
            }
        } catch (error) {
            msg = "서버 통신 중 오류가 발생했습니다.";
        }
        return { msg, flag };
    };

    const nameHandler = (e) => {
        const regExp = /^[가-힣a-zA-Z0-9]{2,}$/;
        let msg, flag;
        const inputVal = e.target.value;

        if (!inputVal) {
            msg = "이름을 입력하세요.";
        } else if (!regExp.test(inputVal)) {
            msg = "2글자 이상의 입력 가능합니다.";
        } else {
            msg = "사용 가능한 이름입니다.";
            flag = true;
        }
        saveInputState({
            key: "userName",
            inputVal,
            msg,
            flag,
        });
    };

    const passwordHandler = (e) => {
        const regExp =
            /^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[!@#$%^&*()-]).{8,25}$/;
        let msg, flag;
        const inputVal = e.target.value;

        if (!inputVal) {
            msg = "비밀번호를 작성해주세요.";
        } else if (!regExp.test(inputVal)) {
            msg = "8글자 이상의 영문, 숫자, 특수문자를 포함해주세요.";
        } else {
            msg = "사용 가능한 비밀번호 입니다.";
            flag = true;
        }

        saveInputState({
            key: "password",
            inputVal,
            msg,
            flag,
        });
    };

    const phoneHandler = (e) => {
        const regExp = /^\d{3}-\d{3,4}-\d{4}$/;
        let msg, flag;
        const inputVal = e.target.value;

        if (!inputVal) {
            msg = "휴대폰 번호를 입력하세요.";
        } else if (!regExp.test(inputVal)) {
            msg = "ex (010-1234-5678)";
        } else {
            msg = "사용 가능한 휴대폰 번호입니다.";
            flag = true;
        }

        saveInputState({
            key: "phoneNum",
            inputVal,
            msg,
            flag,
        });
    };

    // 프로필 이미지 선택 핸들러
    const handleProfileImageChange = (e) => {
        const file = e.target.files[0];

        // 파일이 존재하고 Blob 타입인지 확인
        if (!(file instanceof Blob)) {
            console.error("선택된 파일이 올바르지 않습니다.");
            return;
        }

        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            setProfileImageBlob(file);
            setProfileImage(reader.result);
        };
    };
    const handleRoleClick = (role) => {
        setUserValue({ ...userValue, role: role });
        setSelectedRole(role);
        setCorrect({
            ...correct,
            role: true,
        });
    };

    const saveInputState = ({ key, inputVal, flag, msg }) => {
        setMessage({
            ...message,
            [key]: msg,
        });

        setUserValue({
            ...userValue,
            [key]: inputVal,
        });

        setCorrect({
            ...correct,
            [key]: flag,
        });
    };

    const setAddress = (address) => {
        setUserValue({
            ...userValue,
            streetAddress: address,
        });
        setCorrect({
            ...correct,
            streetAddress: true,
        });
    };

    const setDetailAddress = (detailAddress) => {
        setUserValue({
            ...userValue,
            detailAddress,
        });
        setCorrect({
            ...correct,
            detailAddress: true,
        });
    };

    return (
        <div className="container">
            <div className="wrapper">
                <form>
                    <h1>
                        <Link to="#" className="home">
                            Delivery
                        </Link>
                    </h1>
                    <h2>Register</h2>
                    <div className="input-box">
                        <input
                            type="email"
                            placeholder="Email"
                            required
                            onChange={emailHandler}
                        />
                        <FaUser className="icon" />
                        <span
                            style={
                                correct.email
                                    ? { color: "green" }
                                    : { color: "red" }
                            }
                        >
                            {message.email}
                        </span>
                    </div>

                    <div className="input-box">
                        <input
                            type="password"
                            placeholder="Password"
                            required
                            onChange={passwordHandler}
                            maxLength={25}
                            minLength={8}
                        />
                        <FaLock className="icon" />
                        <span
                            style={
                                correct.password
                                    ? { color: "green" }
                                    : { color: "red" }
                            }
                        >
                            {message.password}
                        </span>
                    </div>
                    <div className="input-box">
                        <input
                            type="text"
                            placeholder="Name"
                            required
                            onChange={nameHandler}
                            minLength={2}
                            maxLength={12}
                        />
                        <PiUserListFill className="icon" />
                        <span
                            style={
                                correct.userName
                                    ? { color: "green" }
                                    : { color: "red" }
                            }
                        >
                            {message.userName}
                        </span>
                    </div>

                    <div className="input-box">
                        <input
                            type="text"
                            placeholder="Address"
                            required
                            value={userValue.streetAddress}
                            readOnly
                        />
                        <FaAddressBook className="icon" />
                        <div className="daum-post-btn">
                            <DaumPost setAddress={setAddress} />
                        </div>
                    </div>
                    <div className="input-box">
                        <input
                            type="text"
                            placeholder="Detailed Address"
                            required
                            onChange={(e) => setDetailAddress(e.target.value)}
                            value={userValue.detailAddress}
                        />
                        <FaRegAddressBook className="icon" />
                    </div>
                    <div className="input-box">
                        <input
                            type="text"
                            placeholder="Phone"
                            required
                            onChange={phoneHandler}
                            maxLength={13}
                        />
                        <IoIosPhonePortrait className="icon" />
                        <span
                            style={
                                correct.phoneNum
                                    ? { color: "green" }
                                    : { color: "red" }
                            }
                        >
                            {message.phoneNum}
                        </span>
                    </div>
                    <div className="role">
                        <div
                            className={`role-button ${
                                selectedRole === "ORGANIZER" ? "selected" : ""
                            }`}
                            onClick={() => handleRoleClick("ORGANIZER")}
                        >
                            사장님
                        </div>
                        <div
                            className={`role-button ${
                                selectedRole === "CUSTOMER" ? "selected" : ""
                            }`}
                            onClick={() => handleRoleClick("CUSTOMER")}
                        >
                            고객
                        </div>
                    </div>
                    <div className="profile">
                        <img
                            src={
                                profileImage
                                    ? profileImage
                                    : require("../../assert/user-icon.png")
                            }
                            alt="profile"
                            className="profile-img"
                        />
                        <label className="signUp-img" htmlFor="profile-img">
                            프로필 사진
                        </label>
                        <input
                            id="profile-img"
                            type="file"
                            accept="image/*"
                            style={{ display: "none" }}
                            onChange={handleProfileImageChange}
                        />
                    </div>

                    <button type="submit" onClick={registerButtonClickHandler}>
                        회원가입
                    </button>
                    <div className="register-link">
                        <Link to="/login" className="login">
                            로그인
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

export default Register;
