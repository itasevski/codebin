package com.ivo.codebin.configuration.security.filters;

import com.ivo.codebin.configuration.security.constants.AuthConstants;
import com.ivo.codebin.model.exception.InvalidCsrfTokenException;
import com.ivo.codebin.service.CookieService;
import com.ivo.codebin.service.CsrfTokenService;
import com.ivo.codebin.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component is used for classes that represent components in our system, like in this case, a filter.
@Component
@AllArgsConstructor
// OncePerRequestFilter is a Spring Web filter that guarantees execution of the filter once per every request.
// We use this filter for both authenticated and non-authenticated requests.
// In our security configuration, we define authenticated paths (requestMatchers) that the user can access. Those requests get checked even before the
// execution of this filter, but in the case when we have a non-authenticated requestMatcher for a given path and the non-authenticated user requests
// a given resource, this filter will get executed. That means that we must handle both scenarios, where the user is authenticated and provides a JWT, and
// when the user is not authenticated.
// So, if this filter gets executed for a non-authenticated user, that means we have a requestMatcher for a path that doesn't require authentication.
public class AuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtService;
    private final CsrfTokenService csrfService;
    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String username;
        final String jwt;
        final String csrfHeader = request.getHeader(AuthConstants.CSRF_HEADER);

        if (request.getCookies() == null || csrfHeader == null || !this.cookieService.authCookieExists(request)) {
            filterChain.doFilter(request, response);
            return;
            // if user is NOT authenticated (accesses public resource, access token cookie has not been provided), execute the other filters and terminate
            // this function. The other filters will determine whether to return the requested resource based on the specified requestMatchers in the configuration.
            // if cookies have been provided but the access or refresh token cookies are not present, it means the user is not authenticated. Execute the other
            // filters and terminate this function.
        }

        if(request.getServletPath().equals("/api/auth/refresh-token"))
            jwt = this.cookieService.extractCookie(request, "refresh-token");
        else jwt = this.cookieService.extractCookie(request, "access-token");
        // otherwise, extract JWT from access or refresh token cookie based on the request URL.
        // since both cookies are going to be sent on every request the user makes (if they're available, i.e if user is authenticated), we need to check the
        // request URL and determine whether we need to generate a new access token by using the refresh token or grab the existing one.


        // This is a very important point during this filter's execution. The "extractUsername" method invokes a parsing function of the JWT, meaning that if
        // the JWT is in some way invalid (does not exist, invalid signature, expired or anything else), the parsing method would throw an exception, therefore
        // terminating this filter's execution right here and returning a 403 Forbidden response to the client. When i say "terminating this filter's execution",
        // I am assuming Spring Security catches the thrown exceptions (from the parser, in this case) and instantly returns a 403 Forbidden response to the client.
        // This means that if the execution progresses past this point, the JWT exists and is not expired, issued by our system and our system's specified private
        // key. If we didn't implement our own ControllerAdvice for general exception handling for our SPECIFIED CUSTOM exceptions, Spring Security would also catch
        // them when thrown and return 403 Forbidden responses to the client.
        username = this.jwtService.extractUsername(jwt); // extract username from JWT

        // Note: if an authenticated user makes a second request with an expired token, we don't need to implement
        // any checks here because we use methods such as "parseClaimsJws" in our JwtService that check the validity of the token (go to method implementation).
        // If token is invalid (expired), exception is thrown and filter execution is immediately executed here, returning a 403 Forbidden response to the client.

        if(username != null && !this.csrfService.isTokenValid(username, csrfHeader))
            throw new InvalidCsrfTokenException("CSRF token is invalid!"); // if token stored in map for user does not match incoming CSRF token, throw exception

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // we don't really need to check whether the context is null, since it's going to be null every time. (context gets cleared after response is returned)
            // we leave it like this in case for example we decide in the future to implement another filter which will update the user's SecurityContext and set it
            // BEFORE this filter.
            // if username is not null, that means that the user IS authenticated (since valid JWT has been provided)
            // Note: the SecurityContextHolder is separate for every thread, every user that authenticates in our system.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // The user has the ability to generate multiple JWTs during his stay on our app, but we guarantee that only one JWT can be active at a time. However,
            // there's a problem. The user can save and use any of the previously generated JWTs, since they are valid, non-expired tokens (assuming that they
            // are used before expiration), created by our system with our system's private key. That is why we must check the used token and see whether it is revoked
            // (when token is revoked, it's also set to expired). If the token has been revoked/expired, we don't add the user in the SecurityContext.
            boolean isTokenNonExpiredAndNonRevoked =
                    this.jwtService.findByToken(jwt)
                            .map(t -> !t.isExpired() && !t.isRevoked())
                            .orElse(false);
            if (this.jwtService.isTokenValid(jwt, userDetails) && isTokenNonExpiredAndNonRevoked) {
                // we get the userDetails and check whether the JWT is valid.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // if the JWT is valid, we create our UsernamePasswordAuthenticationToken, we set some additional details using data from the request and we
                // update the SecurityContext for the user.
            }
        }

        // IF SECURITYCONTEXT IS NULL FOR USER, A 403 FORBIDDEN RESPONSE WILL ALWAYS BE RETURNED TO CLIENT, UNLESS USER ACCESSES A NON-SECURED, PUBLICLY-ACCESSIBLE
        // PATH!!!
        // IF ACCESS/REFRESH TOKEN COOKIE IS NOT PROVIDED BY USER, A 403 FORBIDDEN RESPONSE WILL ALWAYS BE RETURNED TO CLIENT, UNLESS USER ACCESSES A NON-SECURED,
        // PUBLICLY-ACCESSIBLE PATH!!!

        filterChain.doFilter(request, response);
        // if access/refresh token cookie is present but user is not in SecurityContext when accessing a secured resource, a 403 Forbidden response is always
        // returned, and vice versa.
        // That's why we update the user's SecurityContext and then execute the other filters.
    }

}
