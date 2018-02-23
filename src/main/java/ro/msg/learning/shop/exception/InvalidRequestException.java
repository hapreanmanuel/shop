package ro.msg.learning.shop.exception;

public class InvalidRequestException  extends RuntimeException {
    public InvalidRequestException() {
        super("Unable to complete the request");
    }
}
