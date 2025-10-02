import React from 'react'
import {Pagination} from "react-bootstrap";
import {setCurrentPage} from "../../store/features/paginationSlice.js";
import {useDispatch, useSelector} from "react-redux";

// Paginator Component
// This component will handle the pagination of products
// It will display page numbers based on the total number of items and items per page
// Basically separates the products into different pages for easier navigation
const Paginator = () => {
    const dispatch = useDispatch();
    const {itemsPerPage, totalItems, currentPage} = useSelector((state) => state.pagination);
    const paginate = (pageNumber) => {
        dispatch(setCurrentPage(pageNumber));
    }

    let active = currentPage;
    let items = [];

    for (
        let number = 1;
        number <= Math.ceil(totalItems / itemsPerPage);
        number++
    ) {
        items.push(
            <Pagination.Item
                key={number}
                active={number === active}
                onClick={() => paginate(number)}>
                {number}
            </Pagination.Item>
        );
    }

    return (
        <div className='d-flex justify-content-center me-5'>
            <Pagination>{items}</Pagination>
    </div>
  )
}

export default Paginator