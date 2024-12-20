package com.example.security1.Configuration;

import com.example.security1.Service.JWTservice;
import com.example.security1.Service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTservice jwTservice;


    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get the token
        //the token will have two parts bearer + token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        //first extract the token and the username
        if(authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
            username = jwTservice.extractUserName(token);
        }

        //if username != null
        //SecurityContextHolder.getContext().getAuthentication() checks if it authenticated or not
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //first fetch the userdetails
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if(jwTservice.validateToken(token, userDetails)){
                //check user password authentication filter
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //adding the token in the chain
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request, response);
    }
}
