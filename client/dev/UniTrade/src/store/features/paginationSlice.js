import { createSlice } from "@reduxjs/toolkit";

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