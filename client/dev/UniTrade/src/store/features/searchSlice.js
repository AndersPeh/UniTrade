import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {api} from "../../component/services/api";

export const searchByImage = createAsyncThunk(
    "search/searchByImage",
    async (imageFile) => {

            const formData = new FormData();
            formData.append("image", imageFile);

            const response = await api.post("/products/search-by-image", formData);
            return response.data.data; // Assuming the API returns { results: [...] }

    }
)

const initialState = {
  searchQuery: "",
  selectedCategory : "all",
    imageSearch: null,
    imageSearchResults: [],
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
      setImageSearch: (state, action) => {
          state.ImageSearch = action.payload;
      },
      clearFilters: (state) => {
          state.searchQuery = "";
          state.selectedCategory = "all";
            state.imageSearch = null;
            state.imageSearchResults = [];
      },
      setInitialSearchQuery: (state, action) => {
          state.searchQuery = action.payload;
      },
  },
    extraReducers: (builder) => {
        builder
            .addCase(searchByImage.fulfilled, (state, action) => {
                state.imageSearchResults = action.payload;
            });
    },
});

export const {
    setSearchQuery,
    setSelectedCategory,
    clearFilters,
    setImageSearch,
    setInitialSearchQuery,
} = searchSlice.actions;

export default searchSlice.reducer;
