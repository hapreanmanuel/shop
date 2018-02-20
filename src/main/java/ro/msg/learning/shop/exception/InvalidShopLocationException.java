package ro.msg.learning.shop.exception;

public class InvalidShopLocationException extends RuntimeException{
    public InvalidShopLocationException() {
        super("The specified location does not exist.");
    }
}
