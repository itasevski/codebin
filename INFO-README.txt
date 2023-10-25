* This project contains a stateless authentication system with JWT tokens and HttpOnly cookies, as a more modern way of implementing a stateless authentication
system with Spring Security.
* It doesn't support and omits the usage of the traditional "Authorization" header and Bearer tokens and handles the JWTs by setting access and refresh token
HttpOnly cookies in the user's browser.
* It is way more secure than the traditional Bearer token system, since the HttpOnly cookies are sent as part of the request headers, not the body of the request
and they can't be read through JavaScript, thus protecting the user from XSS attacks.