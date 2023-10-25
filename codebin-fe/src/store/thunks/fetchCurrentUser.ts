import { createAsyncThunk } from "@reduxjs/toolkit";
import { fetchUserData } from "../../services/codebinServices";

const fetchCurrentUser = createAsyncThunk("user/fetch", async () => {
  const { data } = await fetchUserData();
  return data;
});

export { fetchCurrentUser };
