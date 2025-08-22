import React from 'react'
import Slider from 'react-slick'
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import bg1 from "../../assets/images/D56_6930.jpg";
import bg2 from "../../assets/images/D56_7343.jpg";


const images = [bg1, bg2];

const HeroSlider = () => {

    const settings = {
        infinite: true,
        speed: 2000,
        autoplay: true,
        autoplayspeed: 16000,
    }
  return (
    <Slider {...settings} className='hero-slider'>
        {images.map((img, index) => (
        <div key={index} className='slide'>
          <img src={img} alt={`Slide ${index + 1}`} className='slide-image' />
        </div>
      ))}
    </Slider>
  )
}

export default HeroSlider