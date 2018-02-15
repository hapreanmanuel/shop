package ro.msg.learning.shop.exception;

public class NoSuitableStrategyException extends RuntimeException {

    public NoSuitableStrategyException(){
        super("Insufficient products in stock");
    }

}
