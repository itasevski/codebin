import axiosCustom from "../util/axiosCustom";
import { getStorageItem } from "./storageServices";

export const fetchUserData = () =>
  axiosCustom.get("/user", {
    headers: {
      "X-XSRF-TOKEN": getStorageItem("csrf_token"),
    },
  });

export const getCsrfToken = () => axiosCustom.get("/csrf/get");

export const register = (
  username: string,
  password: string,
  confirmPassword: string
) =>
  axiosCustom.post("/auth/register", { username, password, confirmPassword });

export const login = (username: string, password: string) =>
  axiosCustom.post("/auth/login", { username, password });

// for testing purposes
export const test = () =>
  axiosCustom.get("/test", {
    headers: {
      "X-XSRF-TOKEN": getStorageItem("csrf_token"),
    },
  });
