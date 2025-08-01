package com.example.shose.server.infrastructure.sercurity.config;

import com.example.shose.server.infrastructure.constant.Roles;
import com.example.shose.server.infrastructure.sercurity.token.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AccountDetalsService accountDetalsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/login-v2/**" ,"/client/**","/cart/**","/admin/poin/**","/cart-detail/**","/admin/promotion","/admin/voucher","/ws/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/admin/**").hasAnyRole("ADMIN","EMLOYEE")
                                .requestMatchers("/admin/bill-detail/**").hasAnyRole("EMLOYEE","ADMIN")
                                .requestMatchers("/admin/bill-history/**").hasAnyRole("EMLOYEE","ADMIN")
                                .requestMatchers("/admin/bill/**").hasAnyRole("EMLOYEE","ADMIN")
                                .requestMatchers("/admin/customer/**").hasAnyRole("EMLOYEE","ADMIN")
                                .requestMatchers("/admin/payment/**").hasAnyRole("EMLOYEE","ADMIN")
                                .requestMatchers("/admin/product/**").hasAnyRole("EMLOYEE","ADMIN")
                                .requestMatchers(HttpMethod.POST,"/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(accountDetalsService.userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        configuration.addAllowedOrigin("http://beeshop.thienlowtech.vn");
        // List<String> allowedOrigins = Arrays.asList("http://beeshop.thienlowtech.vn", "IP");
        // for (String allowedOrigin : allowedOrigins) {
        //     configuration.addAllowedOrigin(allowedOrigin);
        // }
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
