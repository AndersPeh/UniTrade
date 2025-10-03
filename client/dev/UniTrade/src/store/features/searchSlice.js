import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  searchQuery: "",
  selectedCategory : "all"
};

const searchSlice = createSlice({
  name: "search",

  initialState,
    // Reducers for updating search query and selected category
    // Also includes a reducer to clear all filters
    // and a reducer to set the initial search query
    // setIntialSearchQuery is used to set the search query from the URL parameter
    // Basically allows user to click on a product name on the front page and see all products with that name
    // Reducer will update product listing page with that search query
  reducers: {
    setSearchQuery: (state, action) => {
      state.searchQuery = action.payload;
    },
    setSelectedCategory: (state, action) => {
      state.selectedCategory = action.payload;
    },
      clearFilters: (state) => {
          state.searchQuery = "";
          state.selectedCategory = "all";
      },
      setInitialSearchQuery: (state, action) => {
          state.searchQuery = action.payload;
      },
  },
});

export const {
    setSearchQuery,
    setSelectedCategory,
    clearFilters,
    setInitialSearchQuery,
} = searchSlice.actions;

export default searchSlice.reducer;
