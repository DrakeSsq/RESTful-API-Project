package online.store.exceptions;

public class ArticleAlreadyExistsException extends RuntimeException {

  public ArticleAlreadyExistsException() {
    super();
  }

  public ArticleAlreadyExistsException(String message) {
    super(message);
  }

  public ArticleAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
