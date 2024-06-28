package org.app.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class BasicSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signup","/users/register", "/users/login", "/home", "/css/**", "/js/**", "/tasks/**")
                .permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
//                .loginPage("/login") // This line is optional; Spring Boot uses a default login page if not specified.
                .defaultSuccessUrl("/home", true) // Redirect to /home after successful login.
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            );

        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
