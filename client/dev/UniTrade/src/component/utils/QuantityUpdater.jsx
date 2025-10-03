import React from "react";
import { BsDash, BsPlus } from "react-icons/bs";
import {
    decreaseQuantity,
    increaseQuantity,
} from "../../store/features/productSlice";
import { useDispatch, useSelector } from "react-redux";

// QuantityUpdater Component
// This component will handle the quantity update functionality
// It includes buttons to increase or decrease the quantity
// The quantity state is managed in the Redux store
const QuantityUpdater = () => {
    const dispatch = useDispatch();
    const quantity = useSelector((state) => state.product.quantity);
    return (
        <section style={{ width: "150px" }}>
            <div className='input-group'>
                <button
                    onClick={() => dispatch(decreaseQuantity())}
                    className='btn btn-outline-secondary'>
                    {" "}
                    <BsDash />
                </button>

                <input
                    name='quantity'
                    type='number'
                    value={quantity}
                    readOnly
                    className='form-control text-center'></input>

                <button
                    onClick={() => dispatch(increaseQuantity())}
                    className='btn btn-outline-secondary'>
                    {" "}
                    <BsPlus />
                </button>
            </div>
        </section>
    );
};

export default QuantityUpdater;