package by.andersen.intensive.yellow.team.main.controller;

import by.andersen.intensive.yellow.team.main.controller.http.HttpResponse;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.report.ReportNotFoundException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.EmailExistException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.NotAnImageFileException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UserNotFoundException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UsernameExistException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorControllerHandler implements ErrorController {
    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";

    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<HttpResponse> messagingException(MessagingException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = ReportNotFoundException.class)
    public ResponseEntity<HttpResponse> reportNotFoundException(ReportNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotAnImageFileException.class)
    public ResponseEntity<HttpResponse> notAnImageFileException(NotAnImageFileException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @ExceptionHandler(value = DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException() {
        return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(value = LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<HttpResponse> notFound404(NoHandlerFoundException e) {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(value = NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<HttpResponse> serviceException(ServiceException exception) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<HttpResponse> authenticationException(AuthenticationException exception) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}
