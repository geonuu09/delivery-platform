import React from 'react'
import "./OrderMBanner.scss";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Slider from "react-slick";
const OrderMBanner = () => {
    const settings = {
        dots: false,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 3000,
        arrows: false,
    };

    const banners = [
        "https://www.yupdduk.com/bod/config/main/%ED%99%88%ED%8E%98%EC%9D%B4%EC%A7%80%20%EC%82%AC%EC%9D%B4%EB%93%9C%20%EB%B0%B0%EB%84%88_1240x620(%EC%BB%AC%EB%9F%AC%EB%B3%80%EA%B2%BD).png",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGcol1F0orfxF7mcYo5qhmz6CBTRaAQ0gY5Q&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQplBWf6fEG-yDIOuKNQfx46d18cb6dOda2dg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQcrFJEMn7wgvPJ0xnT06E7GB2PsHsgpVIUg&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ460ky1LwDhd0LyaF5AtFYOBNS7_8KRYdTsg&s",
    ];
    return (
        <div className="orderM-banner-container">
            <Slider {...settings}>
                {banners.map((image, index) => (
                    <div key={index} className="orderM-banner-slide">
                        <img
                            src={image}
                            alt={`Banner.png`}
                            className="orderM-banner-image"
                        />
                    </div>
                ))}
            </Slider>
        </div>
    );
}

export default OrderMBanner