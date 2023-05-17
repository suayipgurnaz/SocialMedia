package com.bilgeadam.exception;

import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorMessage;
import com.bilgeadam.exception.ErrorType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

// @ControllerAdvice
@RestControllerAdvice // bu @ResponseBody de içeriyor
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthManagerException.class) //AuthManagerException yakaladıgı zaman buraya dusecek
 // @ResponseBody
    public ResponseEntity<ErrorMessage> handleManagerException(AuthManagerException exception){
        ErrorType errorType=exception.getErrorType();
        HttpStatus httpStatus=errorType.getHttpStatus();
        return new ResponseEntity<>(createError(errorType,exception),httpStatus);
    }
    /** ErrorMessage olusturan method: */
    private ErrorMessage createError(ErrorType errorType, Exception ex) {
        System.out.println("Hata olustu: "+ex.getMessage()); // colsola loglmak için bunu yazdık
        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
        return ResponseEntity.ok("Beklenmeyen bir hata olustu: "+ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class) // DataIntegrityViolationException yakaladıgı zaman buraya dusecek
    public final ResponseEntity<ErrorMessage> handlePsqlException(DataIntegrityViolationException exception){
        ErrorType errorType=ErrorType.USERNAME_DUPLICATE;
        return new ResponseEntity<>(createError(errorType,exception),errorType.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ErrorType errorType=ErrorType.BAD_REQUEST;
        List<String> fields=new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(e ->
                fields.add(e.getField()+": "+e.getDefaultMessage())
        );
        ErrorMessage errorMessage=createError(errorType,exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(errorMessage,errorType.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ErrorMessage> handleMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public final ResponseEntity<ErrorMessage> handleInvalidFormatException(
            InvalidFormatException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentMisMatchException(
            MethodArgumentTypeMismatchException exception) {

        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentMisMatchException(
            MissingPathVariableException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllExceptions(Exception exception) {
        ErrorType errorType = ErrorType.INTERNAL_ERROR;
        List<String> fields = new ArrayList<>();
        fields.add(exception.getMessage());
        ErrorMessage errorMessage = createError(errorType, exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }
}
