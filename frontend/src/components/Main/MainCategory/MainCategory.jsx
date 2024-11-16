import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import "./MainCategory.scss";

const MainCategory = () => {
    return (
        <div className="main-category-container">
            <div className="main-category-wrap">
                <div className="main-category-box">
                    <div>
                        <div>
                            <img
                                src="/images/chicken.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>치킨</p>
                    </div>
                    <div>
                        <div>
                            <img
                                src="/images/jjajangmyeon.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>중식</p>
                    </div>
                    <div>
                        <div>
                            <img
                                src="/images/nigiri.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>돈까스·회</p>
                    </div>
                    <div>
                        <div>
                            <img
                                src="/images/pizza.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>피자</p>
                    </div>
                </div>
                <div className="main-category-box">
                    <div>
                        <div>
                            <img
                                src="/images/burger.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>패스트푸드</p>
                    </div>
                    <div>
                        <div>
                            <img
                                src="/images/bossam.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>족발·보쌈</p>
                    </div>
                    <div>
                        <div>
                            <img
                                src="/images/tteokbokki.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>분식</p>
                    </div>
                    <div>
                        <div>
                            <img
                                src="/images/cake.png"
                                className="main-category-icon"
                            />
                        </div>
                        <p>카페·디저트</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MainCategory;
