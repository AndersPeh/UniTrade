import React from "react";

// StockStatus Component
// This component will display the stock status of a product
// It will show "In stock" if the inventory is greater than 0
// It will show "Out of stock" if the inventory is 0 or less
// The inventory prop is passed from the parent component
const StockStatus = ({ inventory }) => {
  return (
    <p>
      {inventory > 0 ? (
        <span className='text-success'>{inventory} in stock</span>
      ) : (
        <span className='text-danger'>Out of stock</span>
      )}
    </p>
  );
};

export default StockStatus;
