/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.user.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.bondopangaji.springsecurityjwt.error.CustomException;
import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;
import io.bondopangaji.springsecurityjwt.user.service.UserService;
import io.bondopangaji.springsecurityjwt.util.JsonUtil;
import io.bondopangaji.springsecurityjwt.util.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API resources.
 * @see RestController
 * @author Bondo Pangaji
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    /**
     * Dependency injection using lombok @RequiredArgsConstructor.
     * @see RequiredArgsConstructor
     */
    private final UserService userService;

    @GetMapping(
            path = "/user/all",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>>getUser() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping(path = "/user/save",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User>saveUser(@RequestBody User user) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping(path = "/role/save",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Role>saveRole(@RequestBody Role role) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping(
            path = "/role/save-to-user",
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?>saveRoleToUser(
            @RequestBody RoleToUser roleToUser) {
        //
        userService.saveRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/token/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        //
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = JwtUtil.getAlgorithm();
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJwt = verifier.verify(refreshToken);
                String username = decodedJwt.getSubject(    );
                User user = userService.getUserByUsername(username);
                String accessToken = JwtUtil.getAccessToken(request, user, algorithm);
                JsonUtil.jwtTokensResponse(accessToken, refreshToken, response);
            } catch (CustomException ex) {
                throw new CustomException(ex.getMessage());
            }
        } else {
            throw new CustomException("Token not found");
        }
    }

    @Data
    static class RoleToUser {
        String username;
        String roleName;
    }
}
