import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useState, useEffect  } from "react";
import {
    faBook,
    faCircleInfo,
    faFile,
    faHeart,
    faHouse,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate, useLocation  } from "react-router-dom";
import "./Navbar.scss";

const Navbar = () => {
    const [activeItem, setActiveItem] = useState("home");
    const navigate = useNavigate();
    const location = useLocation()
    const handleClick = (item, path) => {
        setActiveItem(item);
        navigate(path);
    };

    useEffect(() => {
        switch (location.pathname) {
            case "/":
                setActiveItem("home");
                break;
            case "/bookmark":
                setActiveItem("bookmark");
                break;
            case "/order":
                setActiveItem("order");
                break;
            case "/myInfo":
                setActiveItem("info");
                break;
            default:
                setActiveItem("home"); 
                break;
        }
    }, [location.pathname]);

    return (
        <div className="navbar-container">
            <div className="navbar-box">
                <div
                    className={`navbar-wrap ${
                        activeItem === "home" ? "active" : ""
                    }`}
                    onClick={() => handleClick("home", "/")}
                >
                    <div className="navbar-icon">
                        <FontAwesomeIcon icon={faHouse} />
                    </div>
                    <div className="navbar-font">홈</div>
                </div>
                <div
                    className={`navbar-wrap ${
                        activeItem === "bookmark" ? "active" : ""
                    }`}
                    onClick={() => handleClick("bookmark", "/bookmark")}
                >
                    <div className="navbar-icon">
                        <FontAwesomeIcon icon={faHeart} />
                    </div>
                    <div className="navbar-font">찜</div>
                </div>
                <div
                    className={`navbar-wrap ${
                        activeItem === "order" ? "active" : ""
                    }`}
                    onClick={() => handleClick("order", "/order")}
                >
                    <div className="navbar-icon">
                        <FontAwesomeIcon icon={faBook} />
                    </div>
                    <div className="navbar-font">주문내역</div>
                </div>
                <div
                    className={`navbar-wrap ${
                        activeItem === "info" ? "active" : ""
                    }`}
                    onClick={() => handleClick("info", "/myInfo")}
                >
                    <div className="navbar-icon">
                        <FontAwesomeIcon icon={faCircleInfo} />
                    </div>
                    <div className="navbar-font">내정보</div>
                </div>
            </div>
        </div>
    );
};

export default Navbar;
