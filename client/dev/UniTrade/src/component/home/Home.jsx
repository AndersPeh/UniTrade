import React, { useEffect, useState } from "react";
import Hero from "../hero/Hero";
import Paginator from "../common/Paginator";
import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";
import ProductImage from "../utils/ProductImage";
import { useSelector, useDispatch } from "react-redux";
import { ToastContainer } from "react-toastify";
import { setTotalItems } from "../../store/features/paginationSlice";
import { getDistinctProductsByName } from "../../store/features/productSlice";
import LoadSpinner from "../common/LoadSpinner";
import StockStatus from "../utils/StockStatus";

const Home = () => {
    const dispatch = useDispatch();
    const [filteredProducts, setFilteredProducts] = useState([]);
    const products = useSelector((state) => state.product.distinctProducts);
    const { searchQuery, selectedCategory, imageSearchResults } = useSelector(
        (state) => state.search
    );
    const { itemsPerPage, currentPage } = useSelector(
        (state) => state.pagination
    );
    const isLoading = useSelector((state) => state.product.isLoading);

    useEffect(() => {
        dispatch(getDistinctProductsByName());
    }, [dispatch]);

    useEffect(() => {
        // If we have image search results, ONLY show those
        if (imageSearchResults.length > 0) {
            setFilteredProducts(imageSearchResults);
            return;
        }

        // Otherwise, apply normal filters
        const results = products.filter((product) => {
            const matchesQuery = product.name
                .toLowerCase()
                .includes(searchQuery.toLowerCase());
            const matchesCategory =
                selectedCategory === "all" ||
                product.category.name
                    .toLowerCase()
                    .includes(selectedCategory.toLowerCase());

            return matchesQuery && matchesCategory;
        });
        setFilteredProducts(results);
    }, [searchQuery, selectedCategory, products, imageSearchResults]);

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
        <div className='home-page'>
            <Hero />
            <div className='products-section'>
                <ToastContainer />
                <div className='d-flex flex-wrap justify-content-center p-5'>
                    {currentProducts && currentProducts.length > 0 ? (
                        currentProducts.map((product) => (
                            <Card key={product.id} className='home-product-card'>
                                <Link to={`/products/${product.name}`} className='link'>
                                    <div className='image-container'>
                                        {product.images.length > 0 && (
                                            <ProductImage productId={product.images[0].id} />
                                        )}
                                    </div>
                                </Link>

                                <Card.Body>
                                    <p className='product-description'>
                                        {product.name} - {product.description}
                                    </p>
                                    <h4 className='price'>{product.price}</h4>
                                    <StockStatus inventory={product.inventory} />
                                    <Link
                                        to={`/products/${product.name}`}
                                        className='shop-now-button'>
                                        Shop now
                                    </Link>
                                </Card.Body>
                            </Card>
                        ))
                    ) : (
                        <div className='no-products-message'>
                            <h3>No products found</h3>
                            <p>Try adjusting your search or filters</p>
                        </div>
                    )}
                </div>
                {/* <Paginator /> */}
            </div>
        </div>
    );
};

export default Home;