import React from "react";
import "./BookMark.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const BookMark = () => {
    return (
        <div className="book-mark-container">
            <div className="book-mark-wrap">
                <div className="book-mark-btn-wrap">
                    <div className="book-mark-h1">찜</div>
                    <div className="book-mark-btn-box">
                        <div>
                            <img src="/images/search.png" />
                        </div>
                        <div>
                            <img src="/images/cart.png" />
                        </div>
                    </div>
                </div>
                <div className="book-mark-box">
                    <div className="book-mark-count">총 2개</div>
                    <div className="book-mark-item-list">
                        <img className="book-mark-img" src="https://png.pngtree.com/thumb_back/fh260/background/20230811/pngtree-mcgs-fried-chicken-in-a-basket-of-condiments-image_13048393.jpg"></img>
                        <div className="book-mark-item">
                            <h2>비비큐 무한리필 부천시청역</h2>
                            <p>
                                <b>4.9</b>
                                <p className="book-mark-review-count">(8)</p>
                                <span>
                                    [튀김도 끝내주는]후라이드, 양념치킨,
                                    고추바사삭, 허니콤보
                                </span>
                            </p>
                            <p>
                                최소주문 
                                <p className="book-mark-price">14900원</p>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BookMark;
