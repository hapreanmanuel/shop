package ro.msg.learning.shop.exception;

public class InvalidLocationException extends RuntimeException{

    public InvalidLocationException() {
        super("Invalid shipment location");
    }
}