package online.store.controller;

import online.store.exception.ErrorMessage;
import online.store.exception.ProductNotFoundException;
import online.store.exception.UpdateProductException;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений для REST API.
 * Перехватывает и обрабатывает исключения, возвращая стандартизированные HTTP-ответы.
 */
public interface ExceptionApiController {

    /**
     * Обрабатывает случаи, когда продукт не найден
     * @param exception исключение ProductNotFoundException
     * @return ответ с сообщением об ошибке и статусом 404 Not Found
     */
    @ExceptionHandler(ProductNotFoundException.class)
    ResponseEntity<ErrorMessage> notFoundException(ProductNotFoundException exception);

    /**
     * Обрабатывает ошибки при обновлении продукта
     * @param exception исключение UpdateProductException
     * @return ответ с сообщением об ошибке и статусом 400 Bad Request
     */
    @ExceptionHandler(UpdateProductException.class)
    ResponseEntity<ErrorMessage> updateProductException(UpdateProductException exception);

    /**
     * Обрабатывает ошибки валидации входных данных
     * @param exception исключение MethodArgumentNotValidException
     * @return ответ с сообщением об ошибке и статусом 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorMessage> notValidException(MethodArgumentNotValidException exception);

    /**
     * Обрабатывает конфликты целостности данных (например, дублирование уникальных значений)
     * @param exception исключение DataIntegrityViolationException
     * @return ответ с сообщением об ошибке и статусом 409 Conflict
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorMessage> notDataIntegrity(DataIntegrityViolationException exception);

    /**
     * Обрабатывает конфликты целостности данных при фильтрации
     * @param exception исключение PathElementException
     * @return ответ с сообщением об ошибке и статусом 409 Conflict
     */
    @ExceptionHandler(PathElementException.class)
    ResponseEntity<ErrorMessage> badAttribute(PathElementException exception);

    /**
     * Обрабатывает конфликты целостности данных при фильтрации а именно поле "type"
     * @param exception исключение HttpMessageNotReadableException
     * @return ответ с сообщением об ошибке и статусом 409 Conflict
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorMessage> badTypeAttribute(HttpMessageNotReadableException exception);
}
