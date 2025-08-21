import {configureStore} from "@reduxjs/toolkit";
import searchReducer from "../store/features/searchSlice";
import categoryReducer from "../store/features/categorySlice";

export const store = configureStore({
  reducer: {
    search: searchReducer,
    category: categoryReducer,
  },
});