package com.sahilkadian.ecommerce.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(List.of(ex.getMessage()));
        errorDetails.setTimeStamp(new Date());
        errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        errorDetails.setTimeStamp(new Date());
        errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleEntityNotFoundException(Exception ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(List.of(ex.getMessage()));
        errorDetails.setTimeStamp(new Date());
        errorDetails.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public final ResponseEntity<ErrorDetails> handleAlreadyExistsException(Exception ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(List.of(ex.getMessage()));
        errorDetails.setTimeStamp(new Date());
        errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public final ResponseEntity<ErrorDetails> handlePasswordMismatchException(Exception ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(List.of(ex.getMessage()));
        errorDetails.setTimeStamp(new Date());
        errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public final ResponseEntity<ErrorDetails> handleTokenExpiredException(Exception ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(List.of(ex.getMessage()));
        errorDetails.setTimeStamp(new Date());
        errorDetails.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
}
