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
      // if Promise.reject is returned from this call, this function is terminated and the Promise.reject is returned

      setEncryptedStorageItem("csrf_token", data.csrf_token);
      // isAuthenticated is already set to true
      // auth cookies are automatically set in browser
      // we just need to update user's CSRF token

      return axiosCustom(originalRequest);
    }
    return Promise.reject(error);
  }
);
// We need to check every response that is received for any errors, more specifically 403 Forbidden errors, and use the user's refresh token
// to update his access token. If there aren't any errors, the response is just returned to the original place where the call/request was made.
// These interceptors are technically Axios middleware between incoming and outgoing requests.
/**
 * 1. The user accesses protected resource, browser sends request to server
 * 2. The server detects expired access token, sends response with status code 403 back to user
 * 3. The Axios response interceptor catches the response, sees that there has been an error and invokes the error callback from this "use" function
 * 4. The interceptor checks whether the error is a 403 Forbidden error, the url is not "/auth/refresh-token" (explained below), the _retry flag is not set and the user is authenticated.
 * If the user is not authenticated, we just return a reject Promise back to where the call/request was made.
 * 5. If the error is a 403 Forbidden error and the user is authenticated, it means the user has an expired access token
 * 6. We set the _retry flag of the original request to the protected resource to "true", as an indicator that it is a retry attempt of accessing the protected resource
 * 7. The "refreshToken" endpoint is called, generating a new access token for the user
 * 8. The interceptor catches the response sent from the server. If there has been an error again, it won't enter the "if" clause since there is a check
 * that ensures that the invoked URL is not "/auth/refresh-token". We do this to avoid an infinite loop in the case there is an error when refreshing the user's access token, i.e. when the user's refresh token is expired.
 * 9. If it is a 200 OK response, it is returned to where the call/request was made (await refreshToken() - line 50)
 * 10. The user's CSRF token is updated and original request is invoked again (axiosCustom(originalRequest)) with new CSRF token.
 * 11. If there is an error for some unknown reason again, the _retry flag is checked and present, meaning that the "if" clause body won't be executed
 * and a reject Promise will be returned.
 */

export default axiosCustom;
