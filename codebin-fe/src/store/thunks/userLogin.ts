import { createAsyncThunk } from "@reduxjs/toolkit";
import { login } from "../../services/codebinServices";

const userLogin = createAsyncThunk(
  "auth/login",
  async (formData: any, { rejectWithValue }) => {
    try {
      const { data } = await login(formData.username, formData.password);
      return data;
    } catch (err: any) {
      return rejectWithValue(
        err.response // if response is not present, it means that either the user's internet is down, or the server is down.
          ? {
              status: err.response.status,
              message: err.response.data.message,
              timestamp: err.response.data.timestamp,
            }
          : {
              status: "N/A",
              message:
                "Network error. Check your internet connection or try again later.",
              timestamp: new Date().toString(),
            }
      ); // avoid thunk wrapper around caught exceptions and return our exception payload
    }
  }
);

export { userLogin };
