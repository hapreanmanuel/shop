package ro.msg.learning.shop.exception;

public class InvalidStockCreationException extends RuntimeException {
    public InvalidStockCreationException() {
        super("Stock entity exists for specified location and product.");
    }
}
