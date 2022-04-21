/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.error.security;

import io.bondopangaji.springsecurityjwt.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Intercept authenticated request who doesn't have access to the requested resource.
 * @see ExceptionTranslationFilter
 * @author Bondo Pangaji
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        //
        log.info(accessDeniedException.getClass().getName());
        ErrorUtil.getSecurityError(
                request,
                response,
                accessDeniedException,
                FORBIDDEN.value(),
                "Forbidden");
    }

}
