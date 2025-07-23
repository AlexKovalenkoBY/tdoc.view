package com.example.tecdoc;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtUtil jwtUtil;
   private final UserDetailsService userDetailsService;

   public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
       this.jwtUtil = jwtUtil;
       this.userDetailsService = uds;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

       final String header = request.getHeader("Authorization");
       if (header != null && header.startsWith("Bearer ")) {
           String token = header.substring(7);
           String username = jwtUtil.extractUsername(token);
           UserDetails userDetails = userDetailsService.loadUserByUsername(username);
           if (jwtUtil.validateToken(token, userDetails)) {
               UsernamePasswordAuthenticationToken auth = 
                   new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(auth);
           }
       }
       filterChain.doFilter(request, response);
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
   }
}
