package online.store.controllers;

import online.store.exceptions.ErrorMessage;
import online.store.exceptions.ProductNotFoundException;
import online.store.exceptions.UpdateProductException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для REST API.
 * Перехватывает и обрабатывает исключения, возвращая стандартизированные HTTP-ответы.
 */
@ControllerAdvice
public class ExceptionApiController {

    /**
     * Обрабатывает случаи, когда продукт не найден
     * @param exception исключение ProductNotFoundException
     * @return ответ с сообщением об ошибке и статусом 404 Not Found
     */
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

    /**
     * Обрабатывает ошибки при обновлении продукта
     * @param exception исключение UpdateProductException
     * @return ответ с сообщением об ошибке и статусом 400 Bad Request
     */
    @ExceptionHandler(UpdateProductException.class)
    public ResponseEntity<ErrorMessage> updateProductException(UpdateProductException exception) {

        ErrorMessage error = new ErrorMessage(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Обрабатывает ошибки валидации входных данных
     * @param exception исключение MethodArgumentNotValidException
     * @return ответ с сообщением об ошибке и статусом 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> notValidException(MethodArgumentNotValidException exception) {

        ErrorMessage error = new ErrorMessage(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Обрабатывает конфликты целостности данных (например, дублирование уникальных значений)
     * @param exception исключение DataIntegrityViolationException
     * @return ответ с сообщением об ошибке и статусом 409 Conflict
     */
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
