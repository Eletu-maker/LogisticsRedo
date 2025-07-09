package com.example.MyLogistics.Config;

import com.example.MyLogistics.Model.Users;
import com.example.MyLogistics.Repository.UserRepo;
import com.example.MyLogistics.Service.JWTServices;
import com.example.MyLogistics.Service.MyUserDetailsService;
import com.example.MyLogistics.Service.TokenBlacklistService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class JWTFilter extends OncePerRequestFilter {


    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenBlacklistService blacklistService;

    @Autowired
    private ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {

                if (blacklistService.isBlacklisted(token)) {
                    logger.warn("Token is blacklisted");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token is blacklisted");
                    return;
                }


                email = jwtServices.extractEmail(token);
                logger.debug("Extracted token for user: {}", email);

            } catch (Exception e) {
                logger.error("Failed to process JWT", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }
        try {
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                Users user = userRepo.findByEmail(email);
                if (user == null || !user.isLogin()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("User is not logged in");
                    return;
                }

                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);

                if (jwtServices.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("JWT validated successfully for user: {}", email);
                }else {
                    logger.warn("Invalid JWT token for user: {}", email);
                }
            }

        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT processing failed", e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        return path.equals("/api/login") || path.equals("/api/register");
    }

}
