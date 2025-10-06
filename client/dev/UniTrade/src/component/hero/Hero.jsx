import React, {useState} from "react";
import HeroSlider from "../hero/HeroSlider";
import SearchBar from "../search/SearchBar";

// Hero banner component
// Basically the main banner of the home page
// It contains the hero slider and search bar component

const Hero = () => {


    const [currentSlide] = useState(0);
    return (
        <div className='hero'>
            <HeroSlider setCurrentSlide={currentSlide} />
            <div className='hero-content'>
                <h1>
                    Welcome to <span className='text-primary'>UniTrade</span>.com
                </h1>
                <SearchBar/>
                <div className='home-button-container'>
                    <a href='/products' className='home-shop-button link'>
                        Shop
                    </a>
                    <button className='deals-button'>Flash Deals</button>
                </div>
            </div>
        </div>
    );
};

export default Hero;