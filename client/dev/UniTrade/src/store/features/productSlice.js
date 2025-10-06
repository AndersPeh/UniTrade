import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api, privateApi } from "../../component/services/api";

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


export const addNewProduct = createAsyncThunk(
    "product/addNewProduct",
    async (product) => {
        const response = await privateApi.post("/products/add", product);
        console.log("The response from the slice : ", response);
        return response.data.data;
    }
);

export const updateProduct = createAsyncThunk(
    "products/updateProduct",
    async ({ productId, updatedProduct }) => {
        const response = await privateApi.put(
            `/products/product/${productId}/update`,
            updatedProduct
        );
        return response;
    }
);

export const deleteProduct = createAsyncThunk(
    "product/deleteProduct",
    async (productId) => {
        const response = await privateApi.delete(
            `/products/product/${productId}/delete`
        );

        return response.data;
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

// Fetch all distinct products by ID from the backend
// This will be used for displaying product details
// when a user clicks on a product in product listing page
export const getProductById = createAsyncThunk(
    "product/getProductById",
    async (productId) => {
        const response = await api.get(`/products/product/${productId}/product`);
        return response.data.data;
    }
);

// Fetch products by category ID from the backend
// This will be used for filtering products based on category
// When a user selects a category from footer
export const getProductsByCategory = createAsyncThunk(
    "product/getProductsByCategory",
    async (categoryId) => {
        const response = await api.get(`/products/category/${categoryId}/products`);
        return response.data.data;
    }
);

const initialState = {
    products: [],
    product: null,
    distinctProducts: [],
    brands: [],
    selectedBrands: [],
    quantity: 1,
    errorMessage: null,
    successMessage: null,
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
        setQuantity: (state, action) => {
            state.quantity = action.payload;
        },
        addBrand: (state, action) => {
            state.brands.push(action.payload);
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
            })
            .addCase(getProductById.fulfilled, (state, action) => {
                state.product = action.payload;
                state.isLoading = false;
            })
            .addCase(getProductsByCategory.fulfilled, (state, action) => {
                state.products = action.payload;
                state.errorMessage = null;
                state.isLoading = false;
            })
            .addCase(addNewProduct.fulfilled, (state, action) => {
                state.products.push(action.payload);
                state.errorMessage = null;
                state.isLoading = false;
            })
            .addCase(updateProduct.fulfilled, (state, action) => {
                state.product = action.payload.data;
                state.errorMessage = null;
                state.isLoading = false;
            })
            .addCase(deleteProduct.fulfilled, (state, action) => {
                state.products = state.products.filter(
                    (product) => product.id !== action.payload.data
                );
            });
    },
});

export const { filterByBrands, setQuantity, addBrand  } = productSlice.actions;
export default productSlice.reducer;
