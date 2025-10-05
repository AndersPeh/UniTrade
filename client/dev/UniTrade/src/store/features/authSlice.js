import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../component/services/api";
import { jwtDecode } from "jwt-decode";

// Async thunk for user login
// Sends a POST request to the /auth/login endpoint with email and password
// On success, it returns the response data containing the access token
export const login = createAsyncThunk(
  "auth/login",
  async ({ email, password }) => {
    const response = await api.post("/auth/login", { email, password });
    return response.data;
  }
);

// Initial state for the auth slice
// It checks localStorage for an existing authToken and userRoles to set the initial state
const initialState = {
  isAuthenticated: !!localStorage.getItem("authToken"),
  token: localStorage.getItem("authToken") || null,
  roles: JSON.parse(localStorage.getItem("userRoles")) || [],
  errorMessage: null,
};

// Auth slice definition
// It includes extraReducers to handle the fulfilled and rejected states of the login thunk
const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(login.fulfilled, (state, action) => {
        const decodedToken = jwtDecode(action.payload.accessToken);

        state.isAuthenticated = true;
        state.token = action.payload.accessToken;
        state.roles = decodedToken.roles;

        localStorage.setItem("authToken", action.payload.accessToken);
        localStorage.setItem("userRoles", JSON.stringify(decodedToken.roles));
        localStorage.setItem("userId", decodedToken.id);
      })
      .addCase(login.rejected, (state, action) => {        
        state.errorMessage = action.error.message;
      });
  },
});

export default authSlice.reducer;
