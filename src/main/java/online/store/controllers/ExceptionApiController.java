package online.store.controllers;

import online.store.exceptions.ErrorMessage;
import online.store.exceptions.ProductNotFoundException;
import online.store.exceptions.UpdateProductException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionApiController {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(ProductNotFoundException exception) {

        ErrorMessage error = new ErrorMessage(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(UpdateProductException.class)
    public ResponseEntity<ErrorMessage> updateProductException(UpdateProductException exception) {

        ErrorMessage error = new ErrorMessage(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> notValidException(MethodArgumentNotValidException exception) {

        ErrorMessage error = new ErrorMessage(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> notDataIntegrity(DataIntegrityViolationException exception) {
        ErrorMessage error = new ErrorMessage(exception.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
