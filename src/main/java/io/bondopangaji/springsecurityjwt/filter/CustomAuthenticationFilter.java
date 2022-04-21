/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.filter;

import com.auth0.jwt.algorithms.Algorithm;
import io.bondopangaji.springsecurityjwt.util.JsonUtil;
import io.bondopangaji.springsecurityjwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom authentication filter,
 * this class will define behavior upon authentication.
 *
 * @author Bondo Pangaji
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * Dependency injection using lombok @RequiredArgsConstructor.
     * @see RequiredArgsConstructor
     */
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        log.info("User `{}` attempting authentication", obtainUsername(request));
        //
        String username = obtainUsername(request);
        String password = obtainPassword(request);


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * Define behavior upon successful authentication,
     * method below will create json web token.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {
        log.info("""
                User `{}` authentication succeed, attempting to generate JWT
                """, obtainUsername(request));
        //
        // Authenticated user from UserDetails
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = JwtUtil.getAlgorithm();

        // Generate access token and refresh token based on authenticated user
        String accessToken = JwtUtil.getAccessToken(request, user, algorithm);
        String refreshToken = JwtUtil.getRefreshToken(request, user, algorithm);

        // JSON response spitting out access_token and refresh_token
        JsonUtil.jwtTokensResponse(accessToken, refreshToken, response);
    }

}
