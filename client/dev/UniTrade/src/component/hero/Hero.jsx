import React, { useState } from "react";
import HeroSlider from "./HeroSlider";
import SearchBar from "../search/SearchBar";
import ImageSearch from "../search/ImageSearch";

const Hero = () => {
    const [showImageSearch, setShowImageSearch] = useState(false);

    return (
        <div className='hero-section'>
            <div className='overlay'></div>
            <div className='hero-content'>
                <h1 className='hero-title'><span className='brand-highlight'>UniTrade</span></h1>
                {/* <HeroSlider /> */}
                <SearchBar onImageSearchClick={() => setShowImageSearch(true)} />
            </div>

            {showImageSearch && (
                <div className='image-search-modal-overlay' onClick={() => setShowImageSearch(false)}>
                    <div className='image-search-modal-content' onClick={(e) => e.stopPropagation()}>
                        <button 
                            className='modal-close-btn' 
                            onClick={() => setShowImageSearch(false)}
                        >
                            ×
                        </button>
                        <h2 className='modal-title'>Search by Image</h2>
                        <ImageSearch onSearchComplete={() => setShowImageSearch(false)} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default Hero;