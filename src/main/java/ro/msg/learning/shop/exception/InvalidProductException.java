package ro.msg.learning.shop.exception;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException() {
        super("The specified product does not exist.");
    }
}
