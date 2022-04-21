/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Utility class to provide error related utility.
 * @author Bondo Pangaji
 */
@UtilityClass
public class ErrorUtil {

    public static <T> Map<String, Object> getApiError(
            final HttpStatus response,
            final Integer statusCode,
            final String message,
            final T obj) {
        //
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("response", response);
        errors.put("status", statusCode);
        errors.put("message", message);
        errors.put("errors", obj);

        return errors;
    }

    public static <T extends Exception> void getSecurityError(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final T obj,
            final Integer status,
            final String message) throws IOException {
        //
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("status", status);
        errors.put("message", message);
        errors.put("error", obj.getLocalizedMessage());
        errors.put("trace", ErrorUtil.getStackTrace(obj));
        errors.put("path", request.getServletPath());

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status);

        new ObjectMapper().writeValue(response.getOutputStream(), errors);

    }

    public static <T extends Throwable> String getStackTrace(final T throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);

        return sw.getBuffer().toString();
    }

}
