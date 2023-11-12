package com.ivo.codebin.configuration;

import com.ivo.codebin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

// @Configuration is used for classes that have bean definitions in them, like in this case, where we create a specific configuration.
@Configuration
@AllArgsConstructor
public class ApplicationConfiguration {

    private final UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // pre-built authentication manager, used for custom authentication (check AuthService)
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // CORS configuration bean for setting the allowed origins, headers and methods on a global level. (recommended approach)
    // This Cross-Origin Resource Sharing configuration specifies a whitelisted origin that can make requests to our server, as well as allows all headers
    // from incoming requests. If a request comes from a non-whitelisted origin, it is going to be rejected. If the request comes from one of the whitelisted
    // origins and contains, for example, a header that isn't allowed, the request will be rejected.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000")); // allow only incoming requests from "http://localhost:3000"
        corsConfiguration.addAllowedHeader("*"); // allow all headers
        // corsConfiguration.setAllowedHeaders(Arrays.asList("Accept", "Access-Control-Allow-Origin", "Content-Type", "Dnt", "Referer", "Authorization", "Sec-Ch-Ua", "Sec-Ch-Ua-Mobile", "Sec-Ch-Ua-Platform", "User-Agent"));
        corsConfiguration.setAllowCredentials(true); // allow cookies to be received
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return configurationSource;
    }

//     example CORS configuration bean that sets all the default permit values to the CORS configuration.
//     This will permit any incoming request to our API to perform a specific action set in the applyPermitDefaultValues() function.
//     We apply this if we want all the default permit values to be set to the CORS policy. This will also mean that any domain could make requests to our API, so we wouldn't need to specify the @CrossOrigin annotation
//     (view the implementation of the applyPermitDefaultValues() function)
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

}
