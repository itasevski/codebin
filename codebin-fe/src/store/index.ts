import { configureStore } from "@reduxjs/toolkit";
import { authReducer } from "./slices/authSlice";
import { clearError } from "./slices/authSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
  },
});

export { store, clearError };
export * from "./thunks/userLogin";
export * from "./thunks/fetchCurrentUser";
