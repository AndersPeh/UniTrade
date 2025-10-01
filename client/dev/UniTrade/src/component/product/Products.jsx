import React, {useEffect, useState} from "react";
import ProductCard from "./ProductCard.jsx";
import SearchBar from "../search/SearchBar.jsx";
import {useDispatch, useSelector} from "react-redux";
import {getAllProducts} from "../../store/features/productSlice.js";
import Paginator from "../common/Paginator.jsx";
import {setTotalItems} from "../../store/features/paginationSlice.js";
import SideBar from "../common/SideBar.jsx";

// Product Page
// This page will contain the search bar and sidebar
// It's also handle the filtering of products based on search query and category
// The filtered products will be passed to the ProductCard component to display the products

const Products = () => {
    const [filteredProducts, setFilteredProducts] = useState([]);
    const dispatch = useDispatch();
    const {products, selectedBrands} = useSelector((state) => state.product);

    const { searchQuery, selectedCategory } = useSelector((state) => state.search);
    const {itemsPerPage, currentPage} = useSelector((state) => state.pagination);

    useEffect(() => {
        dispatch(getAllProducts());
    }, [dispatch]);

    useEffect(() => {
        const results = products.filter((product) => {
            const matchesQuery = product.name
                .toLowerCase()
                .includes(searchQuery.toLowerCase());

            const matchesCategory =
                selectedCategory === "all" ||
                product.category.name
                    .toLowerCase()
                    .includes(selectedCategory.toLowerCase());

            const matchesBrand =
                selectedBrands.length === 0 ||
                selectedBrands.some((selectedBrand) =>
                    product.brand.toLowerCase().includes(selectedBrand.toLowerCase())
                );
            return matchesQuery && matchesCategory && matchesBrand;
        });
        setFilteredProducts(results);
    }, [searchQuery, selectedCategory, products]);

    // Pagination logic
    // Pretty much slice filtered product list to show only a certain number of products per page
    useEffect(() => {
        dispatch(setTotalItems(filteredProducts.length));
    }, [filteredProducts, dispatch]);

    const indexOfLastProduct = currentPage * itemsPerPage;
    const indexOfFirstProduct = indexOfLastProduct - itemsPerPage;
    const currentProducts = filteredProducts.slice(
        indexOfFirstProduct,
        indexOfLastProduct
    );

    return (
        <>
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
            <section style={{flex: 1}}>
                <ProductCard products={currentProducts}/>
                <div className='pagination'><Paginator/></div>
            </section>
        </div>
        </>
        );
};

export default Products;