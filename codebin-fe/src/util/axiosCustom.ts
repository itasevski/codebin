import axios from "axios";
import { refreshToken } from "../services/codebinServices";
import {
  getDecryptedStorageItem,
  setEncryptedStorageItem,
} from "../services/storageServices";
import { isUserAuthenticated } from "../services/userServices";

const axiosCustom = axios.create({
  baseURL: "http://localhost:8080/api",
  // headers: {
  //   "Access-Control-Allow-Origin": "*",
  // },
  // We do NOT need to set the "Access-Control-Allow-Origin" header on the client side. This header is set by the server and returned as a
  // response header if the request comes from a whitelisted origin in our backend CORS configuration. The value of the header is the whitelisted
  // origin that made the request. However, if we decide for some reason to send this header, the CORS configuration must have the header allowed
  // for incoming requests.
});

axiosCustom.defaults.withCredentials = true;
// we default the "withCredentials" property of our custom axios instance to "true", since we want to accept incoming cookies and send outgoing
// cookies. This must go both ways, since this property indicates that we will be able to RECEIVE cookies, as well as SEND them. Axios will send
// only domain-relevant cookies, i.e to the domain that has issued them, which in this case is localhost:8080.

axiosCustom.interceptors.request.use(
  (config) => {
    if (isUserAuthenticated())
      config.headers["X-XSRF-TOKEN"] = getDecryptedStorageItem("csrf_token");

    return config;
  },
  (error) => Promise.reject(error)
);
// When making POST requests with axios, it doesn't include the specified headers, I'm guessing because of some sort of a security policy.
// That's why we create an interceptor for every request (with any method) and we append the user's CSRF token, only if he is authenticated.

axiosCustom.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (
      error.response.status === 403 &&
      error.config.url !== "/auth/refresh-token" &&
      !originalRequest._retry &&
      isUserAuthenticated()
    ) {
      originalRequest._retry = true;

      const { data } = await refreshToken();

      setEncryptedStorageItem("csrf_token", data.csrf_token);
      // isAuthenticated is already set to true
      // auth cookies are automatically set in browser
      // we just need to update user's CSRF token

      return axiosCustom(originalRequest);
    }
    return Promise.reject(error);
  }
);

export default axiosCustom;
