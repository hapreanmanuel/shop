package ro.msg.learning.shop.exception;

public class InsufficientStocksException extends RuntimeException {
    public InsufficientStocksException() {
        super("Insufficient products in stock");
    }
}
