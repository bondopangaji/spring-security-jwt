/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Utility class to provide jwt related utility.
 * @author Bondo Pangaji
 */
@UtilityClass
public class JwtUtil {

    /*
        Storing static key here for simplicity,
        ideally this key would be kept on a separate server.
    */
    private static final String secretKey = "this-is-very-secret";
    // 1h
    private static final long accessTokenExpireLengthMs = 3600000L;
    // 24h
    private static final long refreshTokenExpireLengthMs = 86400000L;

    @Getter @Setter
    private String username;
    @Getter @Setter
    private List<String> authorities;

    public static Algorithm getAlgorithm() {
        // HMAC SHA512
        return Algorithm.HMAC512(secretKey.getBytes());
    }

    public static UsernamePasswordAuthenticationToken getAuthenticationToken(DecodedJWT decodedJWT) {
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public static <T extends User> String getAccessToken(
            HttpServletRequest request,
            T object,
            Algorithm algorithm) {
        //
        setUsername(object.getUsername());
        setAuthorities(object
                .getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        return JWT.create()
                .withSubject(getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpireLengthMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", getAuthorities())
                .sign(algorithm);
    }

    public static <T extends org.springframework.security.core.userdetails.User> String getAccessToken(
            HttpServletRequest request,
            T object,
            Algorithm algorithm) {
        //
        setUsername(object.getUsername());
        setAuthorities(object
                .getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        return JWT.create()
            .withSubject(getUsername())
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpireLengthMs))
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles", getAuthorities())
            .sign(algorithm);
    }

    public static <T extends org.springframework.security.core.userdetails.User> String getRefreshToken(
            HttpServletRequest request,
            T object,
            Algorithm algorithm) {
        //
        setUsername(object.getUsername());

        return JWT.create()
                .withSubject(getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpireLengthMs))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

}
