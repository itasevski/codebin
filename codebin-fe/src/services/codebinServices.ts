import axiosCustom from "../util/axiosCustom";
export const login = (username: string, password: string) =>
  axiosCustom.post("/auth/login", { username, password });
export const register = (
  username: string,
  password: string,
  confirmPassword: string
) =>
  axiosCustom.post("/auth/register", { username, password, confirmPassword });

export const fetchUserData = () => axiosCustom.get("/user");

export const logout = () => axiosCustom.get("/auth/logout");

export const refreshToken = () => axiosCustom.post("/auth/refresh-token");

// for testing purposes
export const test = () => axiosCustom.get("/test");
