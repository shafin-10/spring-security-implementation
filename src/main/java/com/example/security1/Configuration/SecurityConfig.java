package com.example.security1.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//declare it as config class
@Configuration
//to set own configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //first disable csrf
        return httpSecurity
                .csrf(Customizer -> Customizer.disable())

        //for login restriction
        .authorizeHttpRequests(request -> request
                        //this two link will not require authentication
                        .requestMatchers("login","register").permitAll()
                         .anyRequest().authenticated())
        //to enable login with rest client
        .httpBasic(Customizer.withDefaults())
        //make session stateless
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // implement jwtfilter before upaf
        .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        //dao authentication provider is the authentication provider for database
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        //use Bycrypt encoder
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        //user details service to send name password from database for authentication and authorization
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
