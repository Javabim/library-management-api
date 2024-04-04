package com.maidscc.libraryManagementSystem.exception;

import com.maidscc.libraryManagementSystem.payloads.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class MaidSccLibraryExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MaidSccLibraryException.class)
    public ResponseEntity<?> handleMindConnectException(MaidSccLibraryException exception){
        ApiResponse<String> response = new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, response.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        ApiResponse<List<String>> response = new ApiResponse<>();
        response.setData(errors);
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation error");
        response.setStatusCode(status.value());
        return new ResponseEntity<>(response, response.getStatus());
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<String> handleBadCredentialsException(){
        return new ApiResponse<>(
                "Incorrect user details",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse<String> handleExpiredJwtException(ExpiredJwtException e){
        return new ApiResponse<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ApiResponse<String> handleMalformedJwtException(){
        return new ApiResponse<>(
                "Incorrect token",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<String>UsernameNotFoundException(UsernameNotFoundException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.NOT_FOUND);
    }


}
