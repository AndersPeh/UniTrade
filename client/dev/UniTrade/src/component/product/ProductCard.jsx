import React, {useEffect} from "react";
import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";
import ProductImage from "../utils/ProductImage";
import StockStatus from "../utils/StockStatus";
import { deleteProduct } from "../../store/features/productSlice";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";

// Product card component to display list of products
// Basically a template to display each product in a card format
// It receives products as props from the parent component
// and maps through the products array to display each product
// in a card format using react-bootstrap Card component

const ProductCard = ({products}) => {
    const dispatch = useDispatch();
    const userRoles = useSelector((state) => state.auth.roles);
    const isAdmin = userRoles.includes("ROLE_ADMIN");

    const handleDelete = async (productId) => {
        try {
            const result = await dispatch(deleteProduct(productId)).unwrap();
            toast.success(result.message);
        } catch (error) {
            toast.error(error.message);
        }
    };

    return (
        <main className='row m-2'>
            {products.map((product) => (
                <div className='col-12 col-sm-6 col-md-4 col-lg-2' key={product.id}>
                    <Card className='mb-2 mt-2'>
                        <Link to={`/product/${product.id}/details`} className='link'>
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
                            <h4 className='price'>${product.price}</h4>

                            <StockStatus inventory={product.inventory} />

                            <div className='d-flex gap-2'>
                                {isAdmin && (
                                    <>
                                        <Link to={"#"} onClick={() => handleDelete(product.id)}>
                                            delete
                                        </Link>
                                        <Link to={`/update-product/${product.id}/update`}>
                                            edit
                                        </Link>
                                    </>
                                )}

                                <Link
                                    to={`/product/${product.id}/details`}
                                    className='shop-now-button'>
                                    Add to cart
                                </Link>
                            </div>
                        </Card.Body>
                    </Card>
                </div>
            ))}
        </main>
    );
};

export default ProductCard;