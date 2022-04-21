package io.bondopangaji.springsecurityjwt.error.api;

import io.bondopangaji.springsecurityjwt.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * <p>
 *     Custom rest exception handler by Baeldung, to build meaningful error messages for the client,
 *     with the clear goal of giving that client all the info to easily diagnose the problem.
 * </p>
 *
 * @implNote
 * In this implementation, instead of using the ApiError class,
 * I used generic getApiError() to return a Map object of the corresponding error.
 * I also modified a little of the error structure by adding a status code.
 *
 * @see
 * <a href="https://www.baeldung.com/global-error-handler-in-a-spring-rest-api">
 *     Reference [Accessed: April 8, 2022]
 * </a>
 *
 * @author Bondo Pangaji
 */
@Slf4j
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                BAD_REQUEST,
                BAD_REQUEST.value(),
                ex.getLocalizedMessage(),
                errors);

        return handleExceptionInternal(
                ex,
                apiError,
                headers,
                (HttpStatus) apiError.get("response"),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            final BindException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                BAD_REQUEST,
                BAD_REQUEST.value(),
                ex.getLocalizedMessage(),
                errors);

        return handleExceptionInternal(
                ex,
                apiError,
                headers,
                (HttpStatus) apiError.get("response"),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            final TypeMismatchException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error =
                ex.getValue() + " value for " +
                ex.getPropertyName() + " should be of type " +
                ex.getRequiredType();

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                BAD_REQUEST,
                BAD_REQUEST.value(),
                ex.getLocalizedMessage(),
                error);

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            final MissingServletRequestPartException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getRequestPartName() + " part is missing";

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                BAD_REQUEST,
                BAD_REQUEST.value(),
                ex.getLocalizedMessage(),
                error);

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getParameterName() + " parameter is missing";

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                BAD_REQUEST,
                BAD_REQUEST.value(),
                ex.getLocalizedMessage(),
                error);

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    //

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex,
            final WebRequest ignoredRequest) {
        log.info(ex.getClass().getName());
        //
        final String error =
                ex.getName() +" should be of type "+
                Objects.requireNonNull(ex.getRequiredType()).getName();

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                NOT_FOUND,
                NOT_FOUND.value(),
                ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            final ConstraintViolationException ex,
            final WebRequest ignoredRequest) {
        log.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<>();

        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(
                    violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " +
                    violation.getMessage());
        }

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                BAD_REQUEST,
                BAD_REQUEST.value(),
                ex.getLocalizedMessage(),
                errors);

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            final NoHandlerFoundException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        final Map<String, Object> apiError = ErrorUtil.getApiError(
                NOT_FOUND,
                NOT_FOUND.value(),
                ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();

        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");

        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                METHOD_NOT_ALLOWED,
                METHOD_NOT_ALLOWED.value(),
                ex.getLocalizedMessage(),
                builder.toString());

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();

        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");

        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append("  "));

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                UNSUPPORTED_MEDIA_TYPE,
                UNSUPPORTED_MEDIA_TYPE.value(),
                ex.getLocalizedMessage(),
                builder.substring(0, builder.length() - 2));

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

    // 500

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest ignoredRequest) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        //

        final Map<String, Object> apiError = ErrorUtil.getApiError(
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.value(),
                ex.getLocalizedMessage(),
                "error occurred");

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                (HttpStatus) apiError.get("response"));
    }

}