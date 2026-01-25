package com.auhpp.event_management.exception;

import com.auhpp.event_management.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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
    private String processErrorMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            if ("typeMismatch".equals(fieldError.getCode())) {
                return "The uploaded data is not in the correct format (Incorrect data type)";
            }
        }
        return error.getDefaultMessage();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.setCode(HttpStatus.BAD_REQUEST.value());

        if (exception.getErrorCount() == 1) {
            String errorMessage = processErrorMessage(exception.getBindingResult().getAllErrors().getFirst());
            response.setMessage(errorMessage);
        } else {
            List<String> messages = exception.getBindingResult().getAllErrors().stream().map(
                    this::processErrorMessage
            ).toList();
            response.setMessage(messages);
        }
        response.setError(HttpStatus.BAD_REQUEST.name());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // Catch errors when JSON is not formatted correctly (e.g., Incorrect Enum type, missing commas...)
    // Catching a global error when Jackson fails to convert the data.
    // JSON Body and Form data
//    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
//    public ResponseEntity<ApiResponse<Void>> handleJsonErrors(Exception ex) {
//        ApiResponse<Void> response = new ApiResponse<>();
//
//        response.setCode(HttpStatus.BAD_REQUEST.value());
//        response.setError(HttpStatus.BAD_REQUEST.name());
//
//        response.setMessage("The uploaded data is not in the correct format (Incorrect data type)");
//
//        return ResponseEntity.badRequest().body(response);
//    }


    //Handle the remaining exceptions
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(Exception exception) {
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
