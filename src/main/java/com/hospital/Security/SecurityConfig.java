package com.hospital.Security;

import com.hospital.Service.DoctorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

    private final DoctorDetailsService doctorDetailsService;

    public SecurityConfig(DoctorDetailsService doctorDetailsService) {
        this.doctorDetailsService = doctorDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()  // Allow static resources
                .requestMatchers("/doctor/register", "/doctor/login").permitAll()    // Allow doctor login and register pages
                .requestMatchers("/appointments/new" , "/appointments/save").permitAll()                    // ✅ Allow public access to appointment booking
                .requestMatchers("/patient/**").permitAll()                          // Allow patient login and register pages
                .requestMatchers("/doctor/**").hasRole("DOCTOR")                     // Doctor routes
                .anyRequest().authenticated()                                        // All other requests need authentication
            )
            .formLogin(login -> login
                .loginPage("/doctor/login")                  // Doctor login page
                .loginProcessingUrl("/doctor/login")         // URL for processing doctor login
                .defaultSuccessUrl("/doctor/dashboard", true) // Success redirect for doctor login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/doctor/logout", "GET"))
                .logoutSuccessUrl("/doctor/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(doctorDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
