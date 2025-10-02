import { createSlice } from "@reduxjs/toolkit";

// This component handle different pagination states
// like items per page, total items, and current page
// It also includes reducers to update these states
// and a reducer to reset the pagination to its initial state
const initialState = {
    itemsPerPage: 10,
    totalItems: 0,
    currentPage: 1,
};

const paginationSlice = createSlice({
    name: "pagination",
    initialState,
    reducers: {
        setItemsPerPage: (state, action) => {
            state.itemsPerPage = action.payload;
        },
        setTotalItems: (state, action) => {
            state.totalItems = action.payload;
        },
        setCurrentPage: (state, action) => {
            state.currentPage = action.payload;
        },
        resetPagination: (state) => {
            state.currentPage = 1;
            state.totalItems = 0;
            state.itemsPerPage = 10;
        },
    },
});
export const { setItemsPerPage, setTotalItems, setCurrentPage, resetPagination } = paginationSlice.actions;
export default paginationSlice.reducer;