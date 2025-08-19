package online.store.exception;

public class ReceivingSomeoneElseOrderException extends RuntimeException {
    public ReceivingSomeoneElseOrderException(String message) {
        super(message);
    }
}
