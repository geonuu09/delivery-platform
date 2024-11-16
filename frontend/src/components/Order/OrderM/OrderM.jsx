import React from "react";
import "./OrderM.scss";
import OrderMBanner from "../OrderMBanner/OrderMBanner";

const OrderM = () => {
    return (
        <div className="orderM-container">
            <div className="orderM-header-wrap">
                <div className="orderM-btn-wrap">
                    <div className="orderM-h1">주문내역</div>
                    <div className="orderM-btn-box">
                        <div>
                            <img src="/images/search.png" />
                        </div>
                        <div>
                            <img src="/images/cart.png" />
                        </div>
                    </div>
                </div>
                <div className="orderM-search">
                    <img src="/images/search.png" />
                    <input
                        type="text"
                        placeholder="주문했던 메뉴와 가게를 검색해보세요"
                    />
                </div>
            </div>
            <div className="orderM-banner">
                <OrderMBanner />
            </div>

            <div className="orderM-footer-wrap">
                <div className="orderM-item-box">
                    <div className="orderM-item-header">
                        <p>10.20 (일)</p>
                        <button>주문상세</button>
                    </div>
                    <div className="orderM-item-list-wrap">
                        <img className="orderM-item-img" src="data:image/jpeg;base64,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"></img>
                        <div className="orderM-item-wrap">
                            <h2 className="orderM-item-h2">알렉스 플레이스 수제버거 대림점 100000000000호</h2>
                            <p>베이컨 잼 버거 외 4개 36100원</p>
                        </div>
                    </div>
                </div>
                <div className="orderM-item-box">
                    <div className="orderM-item-header">
                        <p>10.20 (일)</p>
                        <button>주문상세</button>
                    </div>
                    <div className="orderM-item-list-wrap">
                        <img className="orderM-item-img" src="https://png.pngtree.com/thumb_back/fh260/background/20230811/pngtree-mcgs-fried-chicken-in-a-basket-of-condiments-image_13048393.jpg"></img>
                        <div className="orderM-item-wrap">
                            <h2 className="orderM-item-h2">비비큐 무한리필 부천시청역</h2>
                            <p>고추바사삭 외 1개 38200원</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderM;
