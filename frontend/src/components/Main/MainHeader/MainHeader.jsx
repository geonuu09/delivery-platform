import React from "react";
import "./MainHeader.scss";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faCartShopping,
    faMagnifyingGlass,
} from "@fortawesome/free-solid-svg-icons";

const MainHeader = () => {
    return (
        <>
            <div className="main-header-container">
                <div className="main-header-wrap">
                    <div className="main-header-btn-wrap">
                        <div className="main-header-address">
                            동작구 신대방길
                        </div>
                        <div className="main-header-btn-box">
                            <div>
                                <img src="/images/search.png" />
                            </div>
                            <div>
                                <img src="/images/cart.png" />
                            </div>
                        </div>
                    </div>
                    <div className="main-header-search-wrap">
                        <input
                            className="main-header-search"
                            placeholder="찾는 메뉴가 뭐예요?"
                        />
                        <div className="main-header-search-icon">
                            <FontAwesomeIcon icon={faMagnifyingGlass} />
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default MainHeader;
