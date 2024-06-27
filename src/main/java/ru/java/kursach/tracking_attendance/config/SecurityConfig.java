package ru.java.kursach.tracking_attendance.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.java.kursach.tracking_attendance.service.JwtAuthenticationFilter;
import ru.java.kursach.tracking_attendance.service.UserService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
// Своего рода отключение CORS (разрешение запросов со всех доменов)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/attendances").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/attendances/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/attendances/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/attendances/mark/{id}").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/disciplines").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/disciplines/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/disciplines/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/groups").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/groups/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/groups/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/lessons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/lessons/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/lessons/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/lessons/types").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/lessons/types/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/lessons/types/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/students/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/students/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/students/{studentId}/groups/{groupId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/students/{studentId}/groups/{groupId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/teachers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/teachers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/teachers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/{username}/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{username}/role").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}

