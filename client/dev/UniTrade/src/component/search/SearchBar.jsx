// import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
// import { getAllCategories } from "../../store/features/categorySlice";
import {setSearchQuery, setSelectedCategory, clearFilters} from "../../store/features/searchSlice.js";
// import { useNavigate, useParams } from "react-router-dom";
// import searchIcon from "../../assets/images/upload.svg";
import ImageSearch from "./ImageSearch";

// Search bar component
// This component will handle the search functionality
// It accepts search query and category selection
// and dispatches actions to update the Redux store
// It also fetches all categories from the backend to populate the category dropdown

const SearchBar = ({ onImageSearchClick }) => {
    const dispatch = useDispatch();
    const { searchQuery, selectedCategory } = useSelector((state) => state.search);
    const categories = useSelector((state) => state.category.categories);

    const handleSearchChange = (e) => {
        dispatch(setSearchQuery(e.target.value));
    };

    const handleCategoryChange = (e) => {
        dispatch(setSelectedCategory(e.target.value));
    };

    const handleClearFilters = () => {
        dispatch(clearFilters());
    };

    return (
        <div className='search-bar-container'>
            <div className='search-bar'>
                <select 
                    value={selectedCategory} 
                    onChange={handleCategoryChange}
                    className='category-select'
                >
                    <option value='all'>All Category</option>
                    {categories.map((category) => (
                        <option key={category.id} value={category.name}>
                            {category.name}
                        </option>
                    ))}
                </select>

                <input
                    type='text'
                    placeholder='search for product(e.g. watch..)'
                    value={searchQuery}
                    onChange={handleSearchChange}
                    className='search-input'
                />

                <button 
                    className='image-search-btn' 
                    onClick={onImageSearchClick}
                    title='Search by Image'
                >
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                        <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                        <circle cx="8.5" cy="8.5" r="1.5"/>
                        <polyline points="21 15 16 10 5 21"/>
                    </svg>
                </button>

                <button 
                    className='clear-filter-btn' 
                    onClick={handleClearFilters}
                >
                    Clear Filter
                </button>
            </div>
        </div>
    );
};

export default SearchBar;