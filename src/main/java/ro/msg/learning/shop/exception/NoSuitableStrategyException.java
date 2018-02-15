package ro.msg.learning.shop.exception;

public class NoSuitableStrategyException extends RuntimeException {

    private static final String MESSAGE = "Insufficient products in stock";

    @Override
    public String getMessage(){
        return MESSAGE;
    }

    public NoSuitableStrategyException(){
        super();
    }

    public NoSuitableStrategyException(String errorMessage, Throwable cause){
        super(errorMessage,cause);
    }

}
