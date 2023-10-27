import axiosCustom from "../util/axiosCustom";
import { getDecryptedStorageItem } from "./storageServices";

export const login = (username: string, password: string) =>
  axiosCustom.post("/auth/login", { username, password });

export const register = (
  username: string,
  password: string,
  confirmPassword: string
) =>
  axiosCustom.post("/auth/register", { username, password, confirmPassword });

export const fetchUserData = () =>
  axiosCustom.get("/user", {
    headers: {
      "X-XSRF-TOKEN": getDecryptedStorageItem("csrf_token"),
    },
  });

export const logout = () =>
  axiosCustom.get("/auth/logout", {
    headers: {
      "X-XSRF-TOKEN": getDecryptedStorageItem("csrf_token"),
    },
  });

// for testing purposes
export const test = () =>
  axiosCustom.get("/test", {
    headers: {
      "X-XSRF-TOKEN": getDecryptedStorageItem("csrf_token"),
    },
  });
