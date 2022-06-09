package jpabook.jpashop.exception;

public class NotenoughStockException extends RuntimeException {

    public NotenoughStockException() {
        super();
    }
    public NotenoughStockException(String message) {
        super(message);

    }
    public NotenoughStockException(Throwable cause) {
        super(cause);
    }

    public NotenoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

}
