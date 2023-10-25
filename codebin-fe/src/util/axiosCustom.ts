import axios from "axios";

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

export default axiosCustom;
