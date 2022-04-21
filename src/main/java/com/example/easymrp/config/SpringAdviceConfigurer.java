package com.example.easymrp.config;

import com.example.easymrp.model.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class SpringAdviceConfigurer {

    @Value("${spring.profiles.active:dev}")
    private String env;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleInvalidFieldExceptions(MethodArgumentNotValidException ex, ServletWebRequest request) {
        if (env.equals("dev"))
            System.out.println("handleInvalidFieldExceptions " + ex.getMessage());

        List<Map> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    HashMap<String, Object> map = new HashMap<>(3);
                    map.put("field", err.getField());
                    map.put("value", err.getRejectedValue());
                    map.put("errorMessage", err.getDefaultMessage());
                    return map;
                })
                .distinct()
                .collect(Collectors.toList());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", fields, request.getRequest().getRequestURI());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(HttpMessageNotReadableException.class)    // JSON Read Exception
    public ErrorResponse handleUnprocessedException(HttpMessageNotReadableException ex,  ServletWebRequest request) {
        if (env.equals("dev"))
            System.out.println("handleUnprocessedException " + ex.getMessage());

        if (ex.getMessage().contains("Required request body is missing"))
            return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Missing Body", "Required request body is missing", request.getRequest().getRequestURI());

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Input Data", ex.getMostSpecificCause().getMessage(), request.getRequest().getRequestURI());
    }

}
