package com.aalonso.CarRegistry.filter;

import com.aalonso.CarRegistry.services.imp.JwtService;
import com.aalonso.CarRegistry.services.imp.UserServiceImp;
import com.mysql.cj.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImp userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if (StringUtils.isEmptyOrWhitespaceOnly(authorizationHeader)) {
            log.warn("JWT token is missing");
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authorizationHeader.substring(7); // El token empieza en la posición 7, bearer antes de este.
        log.info("JWT token: {}", jwtToken);

        userEmail = jwtService.extractEmail(jwtToken);
        if (!StringUtils.isEmptyOrWhitespaceOnly(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
            log.info("JWT token is valid");
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                log.info("User is valid: {}", userDetails);
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}