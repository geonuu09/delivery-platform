import React from "react";
import "./Main.scss";
import MainHeader from "../../components/Main/MainHeader/MainHeader";
import MainCategory from "../../components/Main/MainCategory/MainCategory";
import Banner from "../../components/Main/Banner/Banner";
import RecentOrder from "../../components/Main/RecentOrder/RecentOrder";
import Footer from "../../components/Main/Footer/Footer";

const Main = () => {
    return (
        <div className="main-container">
            <MainHeader />
            <MainCategory />
            <Banner />
            <RecentOrder />
            <Footer/>
        </div>
    );
};

export default Main;
