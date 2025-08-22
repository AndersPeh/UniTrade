import React from "react";
import { FaFacebookF, FaTwitter, FaInstagram } from "react-icons/fa";

const Footer = () => {
    return (
        <footer className='mega-footer'>
        <div className='footer-container'>

            <div className='footer-section'>
                <h3>About us</h3>
                <p>We are UniTrade!</p>
        </div>
            <div className='footer-section'>
                <h3>Category</h3>
                <ul>
                    <li>One</li>
                    <li>Two</li>
                    <li>Three</li>
                </ul>
            </div>
            <div className='footer-section'>
                    <h3>Contact</h3>
                <p>Email: dahed12983@fursee.com</p>
                <p>Phone: 040-000-0000</p>
            </div>
            <div className='footer-section'>
                <h3>Follow Us</h3>
                <a
                    href='https://facebook.com'
                    target='_blank'
                    rel='noopener noreferrer'>
                    <FaFacebookF />
                </a>
                <a
                    href='https://twitter.com'
                    target='_blank'
                    rel='noopener noreferrer'>
                    <FaTwitter />
                </a>
                <a
                    href='https://instagram.com'
                    target='_blank'
                    rel='noopener noreferrer'>
                    <FaInstagram />
                </a>
            </div>
            <div className='footer-bottom'>
                <p>&copy; 2025 unitrade.com. All rights reserved.</p>
            </div>
        </div>
        </footer>
    )
}
export default Footer;