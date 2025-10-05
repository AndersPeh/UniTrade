import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api, privateApi } from "../../component/services/api";

// Async thunk for placing an order
// It sends a POST request to the /orders/user/{userId}/place-order endpoint
// On success, it returns the response data containing the order details
export const placeOrder = createAsyncThunk(
  "order/placeOrder",
  async ({userId}) => {
    // userId : 28
    const response = await api.post(`/orders/user/${userId}/place-order`);
    return response.data;
  }
);

export const fetchUserOrders = createAsyncThunk(
  "orders/fetchUserOrders",
  async (userId) => {
    const response = await api.get(`/orders/user/${userId}/order`);
    return response.data.data;
  }
);

export const createPaymentIntent = createAsyncThunk(
  "payments/createPaymentIntent",
  async ({ amount, currency }) => {
    console.log("createPaymentIntent from the slice :", {amount, currency})
    const response = await api.post("/orders/create-payment-intent", {
      amount,
      currency,
    });
    return response.data;
  }
);

const initialState = {
  orders: [],
  loading: false,
  errorMessage: null,
  successMessage: null,
};

const orderSlice = createSlice({
  name: "order",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(placeOrder.fulfilled, (state, action) => {
        state.orders.push(action.payload.order);
        state.loading = false;
        state.successMessage = action.payload.message;
      })
      .addCase(placeOrder.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(fetchUserOrders.fulfilled, (state, action) => {
        state.orders = action.payload;
        state.loading = false;
      });
  },
});

export default orderSlice.reducer;
