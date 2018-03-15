package ro.msg.learning.shop.exception;

public class InvalidShippmentAddressException extends RuntimeException{
    public InvalidShippmentAddressException() {
        super("Invalid shipment address");
    }
}