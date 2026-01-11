package com.auhpp.event_management.exception;

import com.auhpp.event_management.dto.response.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle app exceptions
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException exception) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.setCode(exception.getErrorCode().getCode());
        response.setMessage(exception.getErrorCode().getMessage());
        response.setError(exception.getErrorCode().name());

        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(response);
    }

    //Handle validation exceptions (@Valid)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.setCode(HttpStatus.BAD_REQUEST.value());
        if (exception.getErrorCount() == 1) {
            response.setMessage(exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
        } else {
            response.setMessage(exception.getBindingResult().getAllErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage
            ).toList());
        }
        response.setError(HttpStatus.BAD_REQUEST.name());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    //Handle the remaining exceptions
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(RuntimeException exception) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatus().value());
        if (exception.getCause() != null)
            response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ": " + exception.getCause().getMessage());
        response.setError(ErrorCode.UNCATEGORIZED_EXCEPTION.name());
        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatus())
                .body(response);
    }

}
