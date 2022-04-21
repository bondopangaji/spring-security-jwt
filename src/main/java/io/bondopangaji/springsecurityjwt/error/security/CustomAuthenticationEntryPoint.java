/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.error.security;

import io.bondopangaji.springsecurityjwt.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Intercept unauthorized request.
 * @see ExceptionTranslationFilter
 * @author Bondo Pangaji
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        //
        log.info(authException.getClass().getName());
        ErrorUtil.getSecurityError(
                request,
                response,
                authException,
                UNAUTHORIZED.value(),
                UNAUTHORIZED.getReasonPhrase());
    }

}