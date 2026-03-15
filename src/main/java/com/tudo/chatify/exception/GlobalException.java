package com.tudo.chatify.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> UserException(UserException e, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setError(e.getMessage());
        errorDetail.setMessage(req.getDescription(false));
        errorDetail.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetail> UserException(MessageException e, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setError(e.getMessage());
        errorDetail.setMessage(req.getDescription(false));
        errorDetail.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> OrtherException(Exception e, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setError(e.getMessage());
        errorDetail.setMessage(req.getDescription(false));
        errorDetail.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> MetghodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e , WebRequest request){
        String error = e.getBindingResult().getFieldError().getDefaultMessage();

        ErrorDetail err = new ErrorDetail();
        err.setError("Validation Error");
        err.setMessage(error);
        err.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(err , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> MessageExceptionHandler(NoHandlerFoundException e, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setError("Endpoint not found");
        errorDetail.setMessage(e.getMessage());
        errorDetail.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

}
