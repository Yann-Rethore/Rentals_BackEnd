package com.openclassroom.Rental.Configuration;

import com.openclassroom.Rental.DTO.UserDTO;
import com.openclassroom.Rental.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDTO user = userRepository.findByEmail(email);
                if (jwtUtil.validateToken(token)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            user, null, null);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
  /* @Override
   protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                   jakarta.servlet.http.@NotNull HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
           throws jakarta.servlet.ServletException, IOException {

       final String requestTokenHeader = request.getHeader("Authorization");

       // JWT Token is in the form "Bearer token". Remove Bearer word and get only the
       // Token
       if (requestTokenHeader == null && !requestTokenHeader.startsWith("Bearer ")) {
           logger.warn("JWT Token does not begin with Bearer String");
       }
       filterChain.doFilter(request, response);
   }*/

