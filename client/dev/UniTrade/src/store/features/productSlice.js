import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../component/services/api";

// Fetch all products from the backend
// This will populate the products array in the Redux store
// and will be used for filtering and displaying products
export const getAllProducts = createAsyncThunk(
    "product/getAllProducts",
    async () => {
        const response = await api.get("/products/all");
        return response.data.data;
    }
);

// Fetch all distinct brands from the backend
export const getAllBrands = createAsyncThunk(
    "product/getAllBrands",
    async () => {
        const response = await api.get("/products/distinct/brands");
        return response.data.data;
    }
);

// Fetch all distinct products by name from the backend
export const getDistinctProductsByName = createAsyncThunk(
    "product/getDistinctProductsByName",
    async () => {
        const response = await api.get("/products/distinct/products");
        return response.data.data;
    }
);

const initialState = {
    products: [],
    distinctProducts: [],
    brands: [],
    selectedBrands: [],
    errorMessage: null,
    isLoading: true,
};
// Product slice
// This slice will handle the state of products, brands, and filtering
// It will also handle the async actions for fetching products and brands
// The filterByBrands reducer will update the selectedBrands array in the state
// based on the user's selection in the sidebar
const productSlice = createSlice({
    name: "product",
    initialState,
    reducers: {
        filterByBrands: (state, action) => {
            const { brand, isChecked } = action.payload;
            if (isChecked) {
                state.selectedBrands.push(brand);
            } else {
                state.selectedBrands = state.selectedBrands.filter((b) => b !== brand);
            }
        },
    },

    extraReducers: (builder) => {
        builder
            .addCase(getAllProducts.fulfilled, (state, action) => {
                state.products = action.payload;
                state.errorMessage = null;
                state.isLoading = false;
            })
            .addCase(getAllProducts.rejected, (state, action) => {
                state.errorMessage = action.error.message;
            })
            .addCase(getAllBrands.fulfilled, (state, action) => {
                state.brands = action.payload;
                state.isLoading = false;
            })
            .addCase(getDistinctProductsByName.fulfilled, (state, action) => {
                state.distinctProducts = action.payload;
                state.isLoading = false;
            });
    },
});

export const { filterByBrands } = productSlice.actions;
export default productSlice.reducer;
