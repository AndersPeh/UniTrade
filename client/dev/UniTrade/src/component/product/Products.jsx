import React, {useEffect, useState} from "react";
import ProductCard from "./ProductCard.jsx";
import SearchBar from "../search/SearchBar.jsx";
import {
    getAllProducts,
    getProductsByCategory,
} from "../../store/features/productSlice";
import {useDispatch, useSelector} from "react-redux";
import Paginator from "../common/Paginator.jsx";
import {setTotalItems} from "../../store/features/paginationSlice.js";
import SideBar from "../common/SideBar.jsx";
import { setInitialSearchQuery } from "../../store/features/searchSlice";
import { useLocation, useParams } from "react-router-dom";
import LoadSpinner from "../common/LoadSpinner";
import { ToastContainer } from "react-toastify";

const Products = () => {
    const [filteredProducts, setFilteredProducts] = useState([]);
    const dispatch = useDispatch();
    const {products, selectedBrands} = useSelector((state) => state.product);
    const { searchQuery, selectedCategory, imageSearchResults } = useSelector(
        (state) => state.search
    );
    const { itemsPerPage, currentPage } = useSelector(
        (state) => state.pagination
    );
    const isLoading = useSelector((state) => state.product.isLoading);

    const { name } = useParams();
    const { categoryId } = useParams();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const initialSearchQuery = queryParams.get("search") || name || "";

    useEffect(() => {
        if (categoryId) {
            dispatch(getProductsByCategory(categoryId));
        } else {
            dispatch(getAllProducts());
        }
    }, [dispatch, categoryId]);

    useEffect(() => {
        dispatch(setInitialSearchQuery(initialSearchQuery));
    }, [initialSearchQuery, dispatch]);

    useEffect(() => {
        // If we have image search results, ONLY show those
        if (imageSearchResults.length > 0) {
            setFilteredProducts(imageSearchResults);
            return;
        }

        // Otherwise, apply normal filters with null safety
        const results = products.filter((product) => {
            const matchesQuery = product.name
                ? product.name.toLowerCase().includes(searchQuery.toLowerCase())
                : false;

            const matchesCategory =
                selectedCategory === "all" ||
                (product.category && product.category.name
                    ? product.category.name
                          .toLowerCase()
                          .includes(selectedCategory.toLowerCase())
                    : false);

            const matchesBrand =
                selectedBrands.length === 0 ||
                (product.brand
                    ? selectedBrands.some((selectedBrand) =>
                          product.brand.toLowerCase().includes(selectedBrand.toLowerCase())
                      )
                    : false);

            return matchesQuery && matchesCategory && matchesBrand;
        });
        setFilteredProducts(results);
    }, [searchQuery, selectedCategory, selectedBrands, products, imageSearchResults]);

    useEffect(() => {
        dispatch(setTotalItems(filteredProducts.length));
    }, [filteredProducts, dispatch]);

    const indexOfLastProduct = currentPage * itemsPerPage;
    const indexOfFirstProduct = indexOfLastProduct - itemsPerPage;
    const currentProducts = filteredProducts.slice(
        indexOfFirstProduct,
        indexOfLastProduct
    );

    if (isLoading) {
        return (
            <div>
                <LoadSpinner />
            </div>
        );
    }

    return (
        <>
            <ToastContainer />
            <div className="d-flex justify-content-center">
                <div className="col-md-6 mt-2">
                    <div className='search-bar-input-group'>
                        <SearchBar/>
                    </div>
                </div>
            </div>
            <div className="d-flex">
                <aside className="sidebar" style={{width: "250px", padding: "1rem"}}>
                    <SideBar/>
                </aside>

                <section style={{ flex: 1 }}>
                    <ProductCard products={currentProducts} />
                </section>
            </div>
            <Paginator />
        </>
    );
};

export default Products;