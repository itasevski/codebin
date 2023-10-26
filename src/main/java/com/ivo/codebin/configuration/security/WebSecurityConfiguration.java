package com.ivo.codebin.configuration.security;

import com.ivo.codebin.configuration.security.filters.AuthFilter;
import com.ivo.codebin.service.LogoutService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration {

    // NOTE: PLEASE READ THE spring-security-important-concepts.txt file in the root directory of this project.

    private final AuthFilter authFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(this.corsConfigurationSource)) // custom CORS configuration
                .csrf(AbstractHttpConfigurer::disable) // we disable Spring Security's CSRF protection, since we implement our own.
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/login", "/api/auth/register") // we have protected routes implemented on the FE, so this is added just in case the user attempts to directly access our REST API.
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                // this "authenticated()" check tells the other filters (after our AuthFilter) to check the SecurityContext for the user and determine whether the
                // user is authenticated, thus protecting any other requested resource other that the specified ones in the requestMatchers.
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        // when setting the session creation policy to STATELESS, we GUARANTEE that sessions won't be created for the authenticated users. This means that after every user's request, his session and the SecurityContext will be cleared.
                        // If, however, the session creation policy is set to ALWAYS (which is stateful), the user's SecurityContext will be persisted throughout every request and state of it will be maintained, which makes the whole stateless JWT authentication concept obsolete.
                .addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class) // add our AuthFilter before the next filter that would be executed after our AuthFilter from the filter chain, which is UsernamePasswordAuthenticationFilter
                .authenticationProvider(this.authenticationProvider) // (tested) the security configuration would work without this, but it is good practice to include an authentication provider in our configuration. We could completely remove our AuthenticationProvider from the system and the configuration would work fine.
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/api/auth/logout") // override the default "/logout" path. This url is by default public, meaning a 403 Forbidden response won't be sent to the client if token is not provided
                                .addLogoutHandler(this.logoutService) // add logout handler. This handler will be invoked before the AuthenticationFilter for the logout request
                                .deleteCookies("access-token", "refresh-token") // delete the access and refresh token cookies on logout.
                                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))) // after successful logout (logout handler has executed successfully), clear the SecurityContext for the user (clear Authentication)
                .build();

        // Note: we can also include the authenticationManager in this configuration, but that would result in a circular reference error, so we leave it as is.
    }

}
