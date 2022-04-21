/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.bondopangaji.springsecurityjwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Custom authorization filter,
 * this class will define behavior upon authorization.
 *
 * @author Bondo Pangaji
 */
@Slf4j
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        //
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/api/v1/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    // Decode JWT
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = JwtUtil.getAlgorithm();
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    // Get security context
                    SecurityContextHolder.getContext().setAuthentication(
                            JwtUtil.getAuthenticationToken(decodedJWT)
                    );

                    // Continue filter
                    filterChain.doFilter(request, response);
                } catch (JWTDecodeException ex) {
                    throw new JWTDecodeException(ex.getMessage());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

}
