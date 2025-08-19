package online.store.exception;

public class InsufficientProductsException extends RuntimeException {
    public InsufficientProductsException(String message) {
        super(message);
    }
}
