import { createSlice } from "@reduxjs/toolkit";
import { userLogin } from "../thunks/userLogin";
import { setStorageItem } from "../../services/storageServices";
import { fetchCurrentUser } from "../thunks/fetchCurrentUser";

const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: null,
    isAuthenticated: false,
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
      state.isLoading = false;

      setStorageItem("csrf_token", action.payload.csrf_token);
    });
    builder.addCase(userLogin.rejected, (state: any, action: any) => {
      state.error = action.payload;
      state.isLoading = false;
    });
    builder.addCase(fetchCurrentUser.fulfilled, (state: any, action: any) => {
      state.user = action.payload;
      state.isAuthenticated = true;

      setStorageItem("csrf_token", action.payload.csrf_token);
    });
    builder.addCase(fetchCurrentUser.rejected, (state: any, action: any) => {
      state.isAuthenticated = false;
    });
  },
});

export const { clearError } = authSlice.actions;
export const authReducer = authSlice.reducer;
