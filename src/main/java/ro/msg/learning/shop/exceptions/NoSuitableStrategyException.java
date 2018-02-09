package ro.msg.learning.shop.exceptions;

public class NoSuitableStrategyException extends RuntimeException {

    private static final String message = "Insufficient products in stock";

    @Override
    public String getMessage(){
        return message;
    }

    public NoSuitableStrategyException(){
        super();
    }

    public NoSuitableStrategyException(String errorMessage, Throwable cause){
        super(errorMessage,cause);
    }

}
