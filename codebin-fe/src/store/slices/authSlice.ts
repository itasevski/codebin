import { createSlice } from "@reduxjs/toolkit";
import { userLogin } from "../thunks/userLogin";
import { setStorageItem } from "../../services/storageServices";
import { fetchCurrentUser } from "../thunks/fetchCurrentUser";

const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: null,
    isLoading: false,
    error: null,
  },
  reducers: {
    clearError: (state: any, action: any) => {
      state.error = null;
    },
  },
  extraReducers(builder) {
    builder.addCase(userLogin.pending, (state: any, action: any) => {
      state.isLoading = true;
    });
    builder.addCase(userLogin.fulfilled, (state: any, action: any) => {
      state.user = { username: action.payload.username, role: action.payload.role };
      state.isLoading = false;

      setStorageItem("csrf_token", action.payload.csrf_token);
      setStorageItem("isAuthenticated", true);
    });
    builder.addCase(userLogin.rejected, (state: any, action: any) => {
      state.error = action.payload;
      state.isLoading = false;
    });

    builder.addCase(fetchCurrentUser.fulfilled, (state: any, action: any) => {
      state.user = { username: action.payload.username, role: action.payload.role };

      setStorageItem("csrf_token", action.payload.csrf_token);
    });
  },
});

export const { clearError } = authSlice.actions;
export const authReducer = authSlice.reducer;
